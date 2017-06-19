import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ClientGUI extends JFrame {

	// Variables declaration - do not modify
	private javax.swing.JButton startCallBtn;
	private javax.swing.JButton endCallBtn;
	private javax.swing.JButton connectBtn;
	private javax.swing.JList connectedClientsList;
	private javax.swing.JButton disconnectBtn;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextField messageField;
	private javax.swing.JTextArea outputArea;
	private javax.swing.JButton recordBtn;
	private javax.swing.JButton sendBtn;
	// End of variables declaration
	Client client;

	public ClientGUI() {
		initComponents();
	}

	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		connectedClientsList = new javax.swing.JList();
		jLabel1 = new javax.swing.JLabel();
		jScrollPane2 = new javax.swing.JScrollPane();
		outputArea = new javax.swing.JTextArea();
		jLabel2 = new javax.swing.JLabel();
		messageField = new javax.swing.JTextField();
		sendBtn = new javax.swing.JButton();
		startCallBtn = new javax.swing.JButton();
		recordBtn = new javax.swing.JButton();
		connectBtn = new javax.swing.JButton();
		disconnectBtn = new javax.swing.JButton();
		endCallBtn = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new java.awt.Dimension(1000, 800));

		connectedClientsList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "" };

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});
		jScrollPane1.setViewportView(connectedClientsList);

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
		jLabel1.setText("Online Users:");

		outputArea.setColumns(20);
		outputArea.setRows(5);
		outputArea.setEditable(false);
		jScrollPane2.setViewportView(outputArea);

		jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
		jLabel2.setText("Chat");

		sendBtn.setText("Send Message");
		sendBtn.setEnabled(false);
		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				sendBtnActionPerformed(actionEvent);
			}
		});

		startCallBtn.setText("Start Call");
		startCallBtn.setEnabled(false);
		startCallBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				startCallBtnActionPerformed(actionEvent);
			}
		});

		recordBtn.setText("Send Recording");
		recordBtn.setEnabled(false);
		recordBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				recordBtnActionPerformed(actionEvent);
			}
		});

		connectBtn.setText("Connect");
		connectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				connectBtnActionPerformed(actionEvent);
			}
		});

		disconnectBtn.setText("Disconnect");
		disconnectBtn.setEnabled(false);
		disconnectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				disconnectBtnActionPerformed(actionEvent);
			}
		});

		endCallBtn.setText("End Call");
		endCallBtn.setEnabled(false);
		endCallBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				endCallBtnActionPerformed(arg0);

			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														jScrollPane1,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														174,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(
														jLabel1,
														javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														174,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jScrollPane2)
												.addComponent(messageField)
												.addGroup(
														layout.createSequentialGroup()
																.addGap(9, 9, 9)
																.addComponent(
																		jLabel2,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		120,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(0,
																		0,
																		Short.MAX_VALUE))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		sendBtn,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		160,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																				.addComponent(
																						startCallBtn,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						140,
																						Short.MAX_VALUE)
																				.addComponent(
																						endCallBtn,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						140,
																						Short.MAX_VALUE))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		recordBtn,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		160,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						connectBtn,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						disconnectBtn,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						109,
																						Short.MAX_VALUE))))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														jLabel1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														33, Short.MAX_VALUE)
												.addComponent(jLabel2))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		jScrollPane2,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		303,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		messageField,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		72,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						recordBtn,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						sendBtn,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										connectBtn)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE)
																								.addComponent(
																										disconnectBtn))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										startCallBtn)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE)
																								.addComponent(
																										endCallBtn))))
												.addComponent(jScrollPane1))
								.addContainerGap()));
		pack();
	}// </editor-fold>

	private void sendBtnActionPerformed(ActionEvent e) {
		String message = messageField.getText();
		String destination = connectedClientsList.getSelectedValue().toString();
		messageField.setText("");
		client.sendMessage(message, destination);
	}

	private void disconnectBtnActionPerformed(ActionEvent evt) {
		try {
			client.disconnect();
			disconnectBtn.setEnabled(false);
			connectBtn.setEnabled(true);
			startCallBtn.setEnabled(false);
			sendBtn.setEnabled(false);
			recordBtn.setEnabled(false);
			connectedClientsList.setListData(new String[] { "" });
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void reset() {
		connectBtn.setEnabled(true);
		disconnectBtn.setEnabled(false);
		startCallBtn.setEnabled(false);
		sendBtn.setEnabled(false);
		recordBtn.setEnabled(false);
	}

	private void connectBtnActionPerformed(ActionEvent evt) {
		client = new Client(JOptionPane.showInputDialog("Server hostname:"),
				3003);
		client.start();
		connectBtn.setEnabled(false);
		disconnectBtn.setEnabled(true);
		startCallBtn.setEnabled(true);
		sendBtn.setEnabled(true);
		recordBtn.setEnabled(true);
	}

	private void startCallBtnActionPerformed(ActionEvent evt) {
		startCallBtn.setEnabled(false);
		endCallBtn.setEnabled(true);
		String destination = (String) connectedClientsList.getSelectedValue();
		if (destination == null) {
			updateOutputArea("Please select a user to call");
			return;
		}
		client.startCall(destination);
	}

	private void endCallBtnActionPerformed(ActionEvent evt) {
		client.endCall();
		startCallBtn.setEnabled(true);
		endCallBtn.setEnabled(false);
	}

	private void recordBtnActionPerformed(ActionEvent evt) {
		new Recording(this);
		recordBtn.setEnabled(false);
	}

	public void updateList(String[] list) {
		Arrays.sort(list);
		connectedClientsList.setListData(list);
	}

	public void updateOutputArea(String message) {
		outputArea.append(message + "\n");
	}

	public void callStarted() {
		endCallBtn.setEnabled(true);
		startCallBtn.setEnabled(false);
	}

	public void callEnded() {
		endCallBtn.setEnabled(false);
		startCallBtn.setEnabled(true);
	}

	public void sendRecording(byte[] toSend) {
		String recipient = (String) connectedClientsList.getSelectedValue();
		client.sendRecording(recipient, toSend);
		recordBtn.setEnabled(true);
	}
}
