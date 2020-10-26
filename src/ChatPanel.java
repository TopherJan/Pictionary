import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.text.DefaultCaret;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
public class ChatPanel extends JPanel implements ActionListener{


  // public JTextPane chatArea = new JTextPane();
  // private SimpleAttributeSet keyWord = new SimpleAttributeSet();
  // private StyledDocument doc = chatArea.getStyledDocument();
  static JTextArea textArea = new JTextArea();
  static JTextField chatTextField = new JTextField(10);
  static JButton chatButton = new JButton("SEND MESSAGE");
  private String chatString = "";
  private ChatClient client;
  int position = 0;

  public ChatPanel(JFrame frame, ChatClient client){
    this.client = client;
    setLayout(null);
    setBackground(new Color(230,230,230));
    setBounds(1051, 92, 230, 596);

    JLayeredPane lp = frame.getLayeredPane();
    JScrollPane scrollingArea = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    scrollingArea.setBounds(1098,182,214,473);
    textArea.setEditable(false);
    lp.add(scrollingArea);

    // DefaultCaret caret = (DefaultCaret)chatArea.getCaret();
    // caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    textArea.setFont(new Font("Calibri", Font.PLAIN, 15));
    textArea.setForeground(new Color(120,120,120));
    chatTextField.setBounds(7, 527, 214, 30);
    chatTextField.addActionListener(this);
    add(chatTextField);

    JLabel chatLabel = new JLabel("CHATBOX", SwingConstants.CENTER);
    chatLabel.setOpaque(true);
    chatLabel.setBackground(new Color(50,50,50));
    chatLabel.setForeground(Color.WHITE);
    chatLabel.setFont(new Font("Impact", Font.PLAIN, 25));
    chatLabel.setBounds(9, 8, 210,37);
    add(chatLabel);

    chatButton.setBounds(7, 558, 214,30);
    chatButton.setBackground(new Color(20,20,20));
    chatButton.setForeground(Color.WHITE);
    chatButton.setFont(new Font("Impact", Font.PLAIN, 17));
    chatButton.addActionListener(this);
    add(chatButton);

    try{

      // StyleConstants.setForeground(keyWord, new Color(75, 178, 74));
      // StyleConstants.setBold(keyWord, true);
     // textArea.append("WELCOME TO " + ChatServer.ip2 + "!\n\n");
    }catch(Exception ex){}
    }

    public void actionPerformed(ActionEvent e) {
      if(!chatTextField.getText().equals("")){
        try{
          chatString = "1" + chatTextField.getText();
          
          if(!chatTextField.getText().equals(ChatClient.guessWord)) {
              client.sendMessages(chatString);
        	  
          }else {
        	  client.sendMessages("1");
          }
          
          Thread.sleep(1);
			if (chatTextField.getText().toLowerCase().equals(ChatClient.guessWord.toLowerCase())) {
				
				textArea.setForeground(Color.GREEN);
				chatButton.setEnabled(false);
                chatTextField.setEnabled(false);
                GamePanel.wordLabel.setText(ChatClient.guessWord);
				client.sendMessages("1" + ChatClient.nameOfPlayer + " GUESSED THE WORD!^");
		
				
				for(int i = 0; i < 8; i ++) {
					if(ChatClient.nameOfPlayer.equals(ScorePanel.playerInfoLabel[i].getText())) {
						position = i;
					}
				}
				client.sendMessages("a"+ position);
			} else {
				System.out.println("false");
			}

          
          // textArea.append()
          // StyleConstants.setBold(keyWord, true);
          // StyleConstants.setForeground(keyWord, new Color(237, 130, 103));
          // doc.insertString(doc.getLength(), "\nYOU: ", keyWord );
          // StyleConstants.setBold(keyWord, false);
          // StyleConstants.setForeground(keyWord, new Color(90,90,90));
          // doc.insertString(doc.getLength(), " " + chatString, keyWord );
          chatTextField.setText("");
        }catch(Exception ex){}
        }
      }
    }
