import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatClient extends Thread {
	private BufferedReader in;
	private PrintWriter out;

	private String IP;
	private static String name;
	private String temp;
	private static String message;
	private String arr[];

	private int x1;
	private int y1;
	private int x2;
	private int y2;

	private int identifier;
	private int turn = 1;
	private int max;
	static String countdown;

	public static int colorStatus;
	public static int fillStatus;
	static float brushThickness = 5f;

	static int a1;
	static int a2;
	static int a3;
	static int a4;
	static int a5;
	static int a6;
	static int a7;
	static int a8;

	static boolean cont = false;

	static String name1 = "";
	static String name2 = "";
	static String name3 = "";
	static String name4 = "";
	static String name5 = "";
	static String name6 = "";
	static String name7 = "";
	static String name8 = "";

	static boolean isArtist = false;

	static String playerMessage = "";
	static String guessWord = "";

	static int finalScore1 = 0;
	static int finalScore2 = 0;
	static int finalScore3 = 0;
	static int finalScore4 = 0;
	static int finalScore5 = 0;
	static int finalScore6 = 0;
	static int finalScore7 = 0;
	static int finalScore8 = 0;

	public static int maxRounds;
	public static int rounds = 1;
	public static boolean endGame = false;
	int index;
    
	static int[] finalScore = new int[8];
  
	static int baseScore = 22;
	int guessCount = 0;
	int playerCount = 0;
	static String nameOfPlayer;
	ArrayList<String> nameList = new ArrayList();
	ArrayList<String> nameList2 = new ArrayList();
	int count = 0;

	public ChatClient(String IP, String name) {
		this.IP = IP;
		this.name = name;
	}

	public void readRandomWord() {
		String line = null;
		int ctr = 1;
		int randomNum = (int) (Math.random() * 459 + 1);

		try {
			FileReader fileReader = new FileReader("words.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				if (ctr == randomNum) {
					if (identifier == turn)
						sendMessages("4" + line);
					break;
				}
				ctr++;
			}

			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void getHighestScore(int[] a) {
		int max = a[0];
		index = 0;

		for (int i = 0; i < a.length; i++) 
		{
			if (max < a[i]) 
			{
				max = a[i];
				index = i;
			}
		}
	}

	public int getTurn() {
		return turn;
	}

	public void sendMessages(String message) {
		out.println(message);
	}

	public void run() {
		try {
			String serverAddress = IP;
			Socket socket = new Socket(serverAddress, 4444);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			while (true) {
				String line = in.readLine();
				String b[] = line.split(" ");

				if (line.startsWith("IDENTIFIER")) {
					identifier = Integer.parseInt(line.substring(11));
				} else if (line.startsWith("SUBMITNAME")) {
					out.println(name);
				} else if (line.startsWith("NAMEACCEPTED")) {
					// textField.setEditable(true);
				} else if (line.startsWith("MESSAGE")) {

					String c[] = line.substring(8).split(" ");
					nameOfPlayer = c[0].substring(0, c[0].length() - 1);
					
			

					if (line.substring(8).charAt(line.substring(8).length() - 1) == '^') {
						ChatPanel.textArea.append(nameOfPlayer + " GUESSED THE WORD!\n");
						if(count == 0) {
							for(int i = 0; i < 8; i++) {
								if(!ScorePanel.playerInfoLabel[i].getText().equals("")) {
									++playerCount;
								}
							}
						}
						count = 1;
						
						++guessCount;
						System.out.println("PLAYER COUNT: " + playerCount);
						System.out.println("GUESS COUNT: " + guessCount);
						
						
						if(guessCount == playerCount-1) {
							
							if (rounds == maxRounds) {
								
								getHighestScore(finalScore);
								endGame = true;
								DrawingPanel.g.fillRect(0, 0, 805, 520);
								GamePanel.timeLabel.setText("00:00");
								GamePanel.wordLabel.setText("");
								ChatPanel.chatTextField.setEnabled(false);
								ChatPanel.chatButton.setEnabled(false);
								DrawingPanel.g.setColor(new Color(30, 30, 30));
								DrawingPanel.g.setFont(new Font("Impact", Font.BOLD, 100));
								DrawingPanel.g.drawString("THE WINNNER IS", 50, 250);
								DrawingPanel.g.drawString("" + ScorePanel.playerInfoLabel[index].getText(), 200, 360);
								
								if(GamePanel.timeLabel.getText().equals("02:00") || GamePanel.timeLabel.getText().equals("00:00")) {
									GamePanel.timeLabel.setText("00:00");
								}
							} else {
								endGame = false;
								
							}
							GamePanel.timeLabel.setText("00:00");
							GamePanel.maxMin = 0;
							GamePanel.maxSec = 0;
							
							
							rounds += 1;
							guessCount = 0;
							System.out.println("ROUNDS: " + baseScore);
							
							
							
						}
					} else {
						if (!(c.length == 1)) {
							ChatPanel.textArea.append(line.substring(8) + "\n");
						}

					}

				} else if (line.startsWith("AVATAR")) {

					String array[] = line.split(" ");
					String finalName = array[2].substring(1, array[2].length());

					if (array[2].charAt(0) == '1') {
						PictionaryFrame.avatar1.setEnabled(true);
						

						a1 = 1;
						if (ScorePanel.ready1 == 1) {
							if (!finalName.equals("")) {
								ScorePanel.playerInfoLabel[0].setText(finalName);
								ScorePanel.playerScoreLabel[0].setText("" + finalScore[0]);

							}
							ScorePanel.playerLabel[0].setEnabled(true);
						}

					} else if (array[2].charAt(0) == '2') {
						PictionaryFrame.avatar2.setEnabled(true);
						
						a2 = 1;
						if (ScorePanel.ready2 == 1) {
							if (!finalName.equals("")) {
								ScorePanel.playerInfoLabel[1].setText(finalName);
								ScorePanel.playerScoreLabel[1].setText("" + finalScore[1]);
							}
							ScorePanel.playerLabel[1].setEnabled(true);

						}
					} else if (array[2].charAt(0) == '3') {

						PictionaryFrame.avatar3.setEnabled(true);
						a3 = 1;
						if (ScorePanel.ready3 == 1) {
							if (!finalName.equals("")) {
								ScorePanel.playerInfoLabel[2].setText(finalName);
								ScorePanel.playerScoreLabel[2].setText("" + finalScore[2]);
							}
							ScorePanel.playerLabel[2].setEnabled(true);
						}

					} else if (array[2].charAt(0) == '4') {
						PictionaryFrame.avatar4.setEnabled(true);
						a4 = 1;
						if (ScorePanel.ready4 == 1) {
							if (!finalName.equals("")) {
								ScorePanel.playerInfoLabel[3].setText(finalName);
								ScorePanel.playerScoreLabel[3].setText("" + finalScore[3]);
							}
							ScorePanel.playerLabel[3].setEnabled(true);
						}

					} else if (array[2].charAt(0) == '5') {
						PictionaryFrame.avatar5.setEnabled(true);
						a5 = 1;
						if (ScorePanel.ready5 == 1) {
							if (!finalName.equals("")) {
								ScorePanel.playerInfoLabel[4].setText(finalName);
								ScorePanel.playerScoreLabel[4].setText("" + finalScore[4]);
							}
							ScorePanel.playerLabel[4].
							
						setEnabled(true);
						}

					} else if (array[2].charAt(0) == '6') {
						PictionaryFrame.avatar6.setEnabled(true);
						a6 = 1;
						if (ScorePanel.ready6 == 1) {
							if (!finalName.equals("")) {
								ScorePanel.playerInfoLabel[5].setText(finalName);
								ScorePanel.playerScoreLabel[5].setText("" + finalScore[5]);
							}
							ScorePanel.playerLabel[5].setEnabled(true);
						}

					} else if (array[2].charAt(0) == '7') {
						PictionaryFrame.avatar7.setEnabled(true);
						a7 = 1;
						if (ScorePanel.ready7 == 1) {
							if (!finalName.equals("")) {
								ScorePanel.playerInfoLabel[6].setText(finalName);
								ScorePanel.playerScoreLabel[6].setText("" + finalScore[6]);
							}
							ScorePanel.playerLabel[6].setEnabled(true);
						}

					} else if (array[2].charAt(0) == '8') {
						PictionaryFrame.avatar8.setEnabled(true);

						a8 = 1;
						if (ScorePanel.ready8 == 1) {
							if (!finalName.equals("")) {
								ScorePanel.playerInfoLabel[7].setText(finalName);
								ScorePanel.playerScoreLabel[7].setText("" + finalScore[7]);
							}
							ScorePanel.playerLabel[7].setEnabled(true);

						}

					}
					
				

				} else if (line.startsWith("POINTS")) {

					String message = line.substring(8);
					String a[] = message.split(" ");

					if (a[1].equals("x1")) {
						x1 = Integer.parseInt(a[2]);
						y1 = Integer.parseInt(a[4]);
						x2 = Integer.parseInt(a[6]);
						y2 = Integer.parseInt(a[8]);
						DrawingPanel.g3.drawLine(x1, y1, x2, y2);
					}

				} else if (line.startsWith("WORD ")) {
					String message = line.substring(8);
					String a[] = line.split(" ");

					ChatPanel.textArea.setForeground(new Color(120, 120, 120));
					ChatPanel.chatTextField.setEnabled(true);

					temp = "";
					for (int i = 0; i < a[2].length(); i++) {
						temp += "_ ";
					}
					if (!(identifier == turn)) {
						GamePanel.wordLabel.setText(temp);

						isArtist = false;
						guessWord = a[2].toLowerCase();
						ChatPanel.chatTextField.setEnabled(true);
						ChatPanel.chatButton.setEnabled(true);

					} else {
						GamePanel.wordLabel.setText(a[2]);
						isArtist = true;
						ChatPanel.chatTextField.setEnabled(false);
						ChatPanel.chatButton.setEnabled(false);
					}

				} else if (line.startsWith("TIME")) {

					String message = line.substring(8);
					String a[] = message.split(" ");

					if (a[1].equals("00:00")) {
						
						if (rounds == maxRounds) {
							
							getHighestScore(finalScore);
							endGame = true;
							DrawingPanel.g.fillRect(0, 0, 805, 520);
							GamePanel.timeLabel.setText("00:00");
							GamePanel.wordLabel.setText("");
							ChatPanel.chatTextField.setEnabled(false);
							ChatPanel.chatButton.setEnabled(false);
							DrawingPanel.g.setColor(new Color(30, 30, 30));
							DrawingPanel.g.setFont(new Font("Impact", Font.BOLD, 100));
							DrawingPanel.g.drawString("THE WINNNER IS", 50, 250);
							DrawingPanel.g.drawString("" + ScorePanel.playerInfoLabel[index].getText(), 200, 360);
							
							if(GamePanel.timeLabel.getText().equals("02:00") || GamePanel.timeLabel.getText().equals("00:00")) {
								GamePanel.timeLabel.setText("00:00");
							}
						} else {
							endGame = false;
						}
						
						
						baseScore = 22;
						guessCount = 0;
						rounds += 1;
						
						ChatClient.isArtist = false;
					}
					
					if(endGame == false) {
						GamePanel.timeLabel.setText(a[1]);						
					}

				} else if (line.startsWith("TURN")) {
					turn += 1;
					if (turn > max) {
						turn = 1;
					}
					System.out.println("TURN" + turn);
					if (identifier == turn) {
						readRandomWord();
					}

				} else if (line.startsWith("MAX")) {
					String arr[] = line.split(" ");
					max = Integer.parseInt(arr[1]);
					// System.out.println(max);
				} else if (line.startsWith("COLORPALETTE")) {
					String message = line.substring(8);
					String a[] = message.split(" ");
					
					if(a[2].equals("clear")) {
						DrawingPanel.clearPanel();
					}else if(a[2].equals("fill")) {
						DrawingPanel.fillPanel(ColorPalettePanel.colors[Integer.parseInt(a[3])]);
					   
					}else if(a[2].equals("delete")) {
						DrawingPanel.g.setColor(ColorPalettePanel.colors[Integer.parseInt(a[3])]);
					}else{
						colorStatus = Integer.parseInt(a[2]);
						brushThickness = Float.parseFloat(a[4]);
					}
				} else if (line.startsWith("COUNTDOWN")) {
					String message = line.substring(8);
					String a[] = message.split(" ");
					countdown = a[2];
					baseScore = 22;

					DrawingPanel.g.setColor(new Color(245, 245, 245));
					DrawingPanel.g.fillRect(0, 0, 805, 520);
					DrawingPanel.g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					DrawingPanel.g.setFont(new Font("Impact", Font.BOLD, 500));
					DrawingPanel.g.setColor(new Color(30, 30, 30));

					if (countdown.equals("6")) {

						DrawingPanel.g.setFont(new Font("Impact", Font.BOLD, 100));
						DrawingPanel.g.drawString("ROUND " + rounds, 240, 300);
					} else {
						DrawingPanel.g.drawString("" + countdown, 250, 450);
					}

					Thread.sleep(1000);

					DrawingPanel.g.setColor(new Color(245, 245, 245));
					DrawingPanel.g.fillRect(0, 0, 805, 520);

				} else if (line.startsWith("CONTINUE")) {
					String message = line.substring(8);
					String a[] = message.split(" ");
					maxRounds = Integer.parseInt(a[2]);
					PictionaryFrame.waiting = false;

					if (PictionaryFrame.tag == false) {
						PictionaryFrame.removeObjects();
						PictionaryFrame.loading();
						try {

							Thread.sleep(1000);
							PictionaryFrame.removeObjects();
						} catch (Exception e) {
						}

						PictionaryFrame.addDrawingPanel();
						PictionaryFrame.refresh();

					}
					cont = true;

				} else if (line.startsWith("DISCONNECT")) {

					a1 = 0;
				} else if (line.startsWith("SCORE")) {
					String array[] = line.split(" ");
					
					baseScore -= 2;
					if (array[2].charAt(0) == '0') {
						finalScore[0] += baseScore;
					}
					if (array[2].charAt(0) == '1') {
						finalScore[1] += baseScore;
					}
					if (array[2].charAt(0) == '2') {
						finalScore[2] += baseScore;
					}
					if (array[2].charAt(0) == '3') {
						finalScore[3] += baseScore;
					}
					if (array[2].charAt(0) == '4') {
						finalScore[4] += baseScore;
					}
					if (array[2].charAt(0) == '5') {
						finalScore[5] += baseScore;
					}
					if (array[2].charAt(0) == '6') {
						finalScore[6] += baseScore;
					}
					if (array[2].charAt(0) == '7') {
						finalScore[7] += baseScore;
					}
					
					
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
