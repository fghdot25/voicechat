import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;

public class Recording extends JFrame {

	private javax.swing.JButton recordBtn;
	private javax.swing.JButton sendBtn;
	private javax.swing.JButton stopBtn;
	private javax.swing.JButton playbackBtn;

	boolean stopCapture = false;
	ByteArrayOutputStream byteArrayOutputStream;
	AudioFormat audioFormat;
	TargetDataLine mic;
	AudioInputStream audioInputStream;
	SourceDataLine output;

	ClientGUI clientGui;
	
	byte[] recording;

	public Recording(ClientGUI gui) {
		initComponents();
		this.setVisible(true);

		recordBtn.setEnabled(true);
		playbackBtn.setEnabled(false);

		clientGui = gui;
	}

	public Recording(ClientGUI gui, byte[] recording) {
		initComponents();
		this.setVisible(true);
		recordBtn.setEnabled(false);
		clientGui = gui;
		this.recording = recording;
	}

	private void initComponents() {

		recordBtn = new javax.swing.JButton();
		stopBtn = new javax.swing.JButton();
		sendBtn = new javax.swing.JButton();
		playbackBtn = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		recordBtn.setText("Record");
		recordBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				recordBtnActionPerformed(evt);
			}
		});

		stopBtn.setText("Stop");
		stopBtn.setEnabled(false);
		stopBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				stopBtnActionPerformed(evt);
			}
		});

		sendBtn.setText("Send");
		sendBtn.setEnabled(false);
		sendBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendBtnActionPerformed(evt);
			}
		});

		playbackBtn.setText("Play Recording");
		playbackBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				playbackBtnActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(111, 111, 111)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(
														playbackBtn,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														196, Short.MAX_VALUE)
												.addGroup(
														layout.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
																.addComponent(
																		stopBtn,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addComponent(
																		recordBtn,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		196,
																		Short.MAX_VALUE)
																.addComponent(
																		sendBtn,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)))
								.addContainerGap(118, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addGap(24, 24, 24)
						.addComponent(recordBtn,
								javax.swing.GroupLayout.PREFERRED_SIZE, 43,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(stopBtn,
								javax.swing.GroupLayout.PREFERRED_SIZE, 43,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(sendBtn,
								javax.swing.GroupLayout.PREFERRED_SIZE, 48,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(36, 36, 36)
						.addComponent(playbackBtn,
								javax.swing.GroupLayout.DEFAULT_SIZE, 58,
								Short.MAX_VALUE).addContainerGap()));
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		pack();
	}

	private void recordBtnActionPerformed(java.awt.event.ActionEvent evt) {
		recordBtn.setEnabled(false);
		sendBtn.setEnabled(false);
		stopBtn.setEnabled(true);

		try {
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(
					TargetDataLine.class, audioFormat);
			mic = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			mic.open(audioFormat);
			mic.start();

			Thread captureThread = new Thread(new CaptureThread());
			captureThread.start();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}

	private void stopBtnActionPerformed(java.awt.event.ActionEvent evt) {
		sendBtn.setEnabled(true);
		recordBtn.setEnabled(true);
		stopBtn.setEnabled(false);
	}

	private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {
		byte[] toSend = byteArrayOutputStream.toByteArray();
		clientGui.sendRecording(toSend);
		this.dispose();
	}

	private void playbackBtnActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			InputStream byteArrayInputStream = new ByteArrayInputStream(
					recording);
			AudioFormat audioFormat = getAudioFormat();
			audioInputStream = new AudioInputStream(byteArrayInputStream,
					audioFormat, recording.length / audioFormat.getFrameSize());
			DataLine.Info dataLineInfo = new DataLine.Info(
					SourceDataLine.class, audioFormat);
			output = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			output.open(audioFormat);
			output.start();

			Thread playThread = new Thread(new PlayThread());
			playThread.start();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	class CaptureThread extends Thread {
		byte temp[] = new byte[10000];

		public void run() {
			byteArrayOutputStream = new ByteArrayOutputStream();
			stopCapture = false;
			try {
				while (!stopCapture) {
					int cnt = mic.read(temp, 0, temp.length);
					if (cnt > 0) {
						byteArrayOutputStream.write(temp, 0, cnt);
					}
				}
				byteArrayOutputStream.close();
				mic.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	class PlayThread extends Thread {
		byte temp[] = new byte[10000];

		public void run() {
			try {
				int cnt;
				while ((cnt = audioInputStream.read(temp, 0, temp.length)) != -1) {
					if (cnt > 0) {
						output.write(temp, 0, cnt);
					}
				}
				output.drain();
				output.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
