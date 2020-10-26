import java.awt.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.DefaultCaret;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class GamePanel extends JPanel implements Runnable {

	public static int x1;
	public static int y1;
	public static int x2;
	public static int y2;

	protected static boolean running;
	protected static volatile boolean displayMenu = false;

	static int maxMin = 2;
	static int maxSec = 59;

	private ChatClient client;

	static String currentString;

	static int serverCount = 0;

	public static JLabel timeLabel = new JLabel("00:00");
	public static JLabel wordLabel = new JLabel("", SwingConstants.CENTER);

	private JPanel timePanel = new BackgroundPanel("img/timePanel.png");
	private JPanel wordPanel = new BackgroundPanel("img/wordPanel.png");
	private JPanel optionPanel = new BackgroundPanel("img/optionPanel.png");
	private JPanel chatPanel = new JPanel(null);

	private JLabel exitLabel = new JLabel(new ImageIcon("img/exit.png"));
//	private JLabel backLabel = new JLabel(new ImageIcon("img/back.png"));
//	private JLabel musicLabel = new JLabel(new ImageIcon("img/music.png"));

	static JTextArea textArea = new JTextArea();
	private JTextField chatTextField = new JTextField(10);
	private JButton chatButton = new JButton("SEND MESSAGE");
	private String chatString = "";



	private MyListener listener = new MyListener();



	public GamePanel(ChatClient client) {
		this.client = client;
		setLayout(null);
		setBounds(4, 4, 1277, 700);
		setBackground(new Color(0, 0, 0, 0));



		optionPanel.setBounds(1047, 0, 230, 83);
		optionPanel.setBackground(new Color(0, 0, 0, 0));
		add(optionPanel);

//		musicLabel.setBounds(4, 4, 70, 74);
//		optionPanel.add(musicLabel);
//
//		backLabel.setBounds(78, 4, 73, 74);
//		optionPanel.add(backLabel);

		exitLabel.setBounds(155, 4, 70, 74);
		optionPanel.add(exitLabel);

		exitLabel.addMouseListener(listener);
//		backLabel.addMouseListener(listener);
//		musicLabel.addMouseListener(listener);

		timePanel.setBackground(new Color(0, 0, 0, 0));
		timePanel.setBounds(0, 0, 1040, 768);
		add(timePanel);

		timeLabel.setBounds(48, 6, 170, 70);
		timeLabel.setForeground(new Color(240, 240, 240));
		timeLabel.setFont(new Font("Impact", Font.PLAIN, 57));
		timePanel.add(timeLabel);

		wordPanel.setBackground(new Color(0, 0, 0, 0));
		wordPanel.setBounds(235, 0, 805, 82);
		add(wordPanel);

		wordLabel.setBounds(0, 6, 805, 70);
		wordLabel.setForeground(new Color(240, 240, 240));
		wordLabel.setFont(new Font("Impact", Font.PLAIN, 57));
		wordPanel.add(wordLabel);

	}



	public void run() {

		maxMin -= 1;
		// WordPanel.readRandomWord();

		try {

			client.countdown = "5";
			Thread.sleep(3000);
			while (serverCount == 1 && ChatClient.endGame == false) {


				Thread.sleep(1000);
				currentString = "0" + maxMin + ":00";

				if (maxSec > 9) {
					currentString = "0" + maxMin + ":" + maxSec;
					maxSec -= 1;
				} else if (maxSec <= 9 && maxSec >= 0) {
					currentString = "0" + maxMin + ":0" + maxSec;
					maxSec -= 1;
				}


				if (maxSec == -1 && maxMin > 0) {
					currentString = "0" + maxMin + ":00";
					maxSec = 59;
					maxMin -= 1;

				}
				if (timeLabel.getText().equals("00:00")) {

					maxMin = 2;
					maxSec = 0;
					currentString = "0" + maxMin + ":0" + maxSec;
					if(ChatClient.endGame == false) {
						client.countdown = "5";
						for (int i = 6; i >= 0; i--) {


							client.sendMessages("8" + i);
							client.countdown = "" + i;

							Thread.sleep(1500);

						}
						client.countdown = "5";
						client.sendMessages("6");

					}

				}
				client.sendMessages("5" + currentString);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class MyListener extends MouseAdapter {
		public void mouseExited(MouseEvent event) {
			if (event.getSource() == exitLabel) {
				exitLabel.setIcon(new ImageIcon("img/exit.png"));
			}
//			if (event.getSource() == backLabel) {
//				backLabel.setIcon(new ImageIcon("img/back.png"));
//			}
//			if (event.getSource() == musicLabel) {
//				musicLabel.setIcon(new ImageIcon("img/music.png"));
//			}

		}

		public void mouseEntered(MouseEvent event) {
			if (event.getSource() == exitLabel) {
				exitLabel.setIcon(new ImageIcon("img/exitInv.png"));
			}
//			if (event.getSource() == backLabel) {
//				backLabel.setIcon(new ImageIcon("img/backInv.png"));
//			}
//			if (event.getSource() == musicLabel) {
//				musicLabel.setIcon(new ImageIcon("img/musicInv.png"));
//			}

		}

		public void mouseClicked(MouseEvent event) {

			if (event.getSource() == exitLabel) {
				int result = JOptionPane.showConfirmDialog(null, "ARE YOU SURE?", "CONFIRM", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION) {
					client.sendMessages("0");
					System.exit(0);

				} else {
					// Do nothing
				}
			}
		}

	}


}
