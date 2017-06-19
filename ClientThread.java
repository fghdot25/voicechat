import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {

	private Socket socket;
	private String hostname;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private final int CONNECT = 0;
	private final int DISCONNECT = 1;
	private final int UPDATE = 2;
	private final int BROADCAST_MESSAGE = 3;
	private final int CALL = 4;
	private final int END_CALL = 5;
	private final int ERROR = 6;
	private final int RECORDING = 7;
	private final int PRIVATE_MESSAGE = 8;
	boolean listening;
	int id;

	public ClientThread(Socket socket, int num) {
		this.socket = socket;
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			System.out.println(e);
		}
		this.hostname = socket.getInetAddress().getHostName();
		listening = true;
		this.id = num;
	}

	public void run() {
		Server.connectClient(this);
		broadcastMessage(CONNECT, hostname);
		updateClients();
		while (listening) {
			try {
				int command = inputStream.readInt();
				if (command == DISCONNECT) {
					Server.disconnectClient(id);
					broadcastMessage(DISCONNECT, hostname);
					updateClients();
					listening = false;
					break;
				} else if (command == BROADCAST_MESSAGE) {
					String message = (String) inputStream.readObject();
					broadcastMessage(BROADCAST_MESSAGE, hostname + ": " + message);
				} else if (command == CALL) {
					String destination = (String) inputStream.readObject();
					setUpCall(destination);				
				} else if (command == END_CALL) {
					String destination = (String) inputStream.readObject();
					endCall(destination);
					Server.updateGUI(hostname + " terminated a call with "
							+ destination);
				} else if (command == RECORDING) {
					String recipient = (String) inputStream.readObject();
					byte[] recording = (byte[]) inputStream.readObject();
					sendIndividualMessage(recipient, RECORDING, recording);
					Server.updateGUI(hostname + " sent a recording to " + recipient);
				} else if (command == PRIVATE_MESSAGE) {
					String recipient = (String) inputStream.readObject();
					String message = (String) inputStream.readObject();					
					sendIndividualMessage(recipient, PRIVATE_MESSAGE, message);
					Server.updateGUI(hostname + " sent a message to " + recipient);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateClients() {
		String clientList[] = new String[Server.clients.size()];
		int count = 0;
		for (ClientThread c : Server.clients) {
			clientList[count] = c.getHostname();
			count++;
		}
		broadcastMessage(UPDATE, clientList);
	}

	private void broadcastMessage(int type, Object message) {
		for (ClientThread c : Server.clients) {
			try {
				c.outputStream.writeInt(type);
				c.outputStream.writeObject(message);
			} catch (IOException e) {
			}
		}
	}

	private void setUpCall(String destination) {
		for (ClientThread c : Server.clients) {
			if (c.getHostname().equals(destination)) {
				try {
					if (c == this) {
						c.outputStream.writeInt(ERROR);
						outputStream
								.writeObject("Cannot initiate a voice call with yourself");
					} else {
						c.outputStream.writeInt(CALL);
						c.outputStream.writeObject(hostname);
						Server.updateGUI(hostname + " initiated a call with "
								+ destination);
					}
				} catch (IOException ex) {
					System.out.println(ex);
				}
			}
		}
	}

	private void endCall(String destination) {
		for (ClientThread c : Server.clients) {
			if (c.getHostname().equals(destination)) {
				try {
					c.outputStream.writeInt(END_CALL);
					c.outputStream.writeObject(hostname);
				} catch (Exception e) {

				}
			}
		}
	}
	
	private void sendIndividualMessage(String recipient, int type, Object obj) {
		for (ClientThread c : Server.clients) {
			if(c.getHostname().equals(recipient)) {
				try {
					c.outputStream.writeInt(type);
					c.outputStream.writeObject(hostname);
					c.outputStream.writeObject(obj);
				} catch (Exception e) {
					
				}
			}
		}
	}

	public String getHostname() {
		return hostname;
	}
}
