import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ColorPalettePanel extends JPanel implements Runnable {
	public static Color[] colors = { new Color(30, 30, 30), new Color(128, 128, 128), new Color(141, 96, 56),
			new Color(225, 81, 60), new Color(227, 132, 35), new Color(243, 193, 29), new Color(75, 178, 74),
			new Color(80, 159, 218), new Color(40, 126, 187), new Color(135, 79, 159), new Color(255, 255, 255),
			new Color(255, 255, 255), new Color(255, 255, 255), new Color(255, 255, 255), new Color(180, 180, 180),
			new Color(196, 148, 104), new Color(232, 122, 106), new Color(234, 159, 85), new Color(248, 219, 118),
			new Color(159, 215, 159), new Color(173, 210, 237), new Color(148, 197, 233), new Color(182, 141, 199),
			new Color(255, 255, 255), new Color(245, 245, 245), new Color(255, 255, 255) };

	public static float brushThickness = 5f;
	public static int status = 0;
	public static int currentColor = 0;
	public static int statusFill = 0;

	private int panelSize = 26;
	private JPanel[] colorPanel = new JPanel[panelSize];
	private JLabel[] brushLabel = new JLabel[6];

	private MyMouse myMouse = new MyMouse();
	ChatClient client;

	public ColorPalettePanel(ChatClient client) {

		this.client = client;
		setBounds(240, 618, 805, 70);
		setLayout(new GridLayout(2, 10, 5, 5));
		setBackground(new Color(0, 0, 0, 0));

		for (int i = 0, j = 0; i < panelSize; i++) {

			if (i < 10) {
				colorPanel[i] = new JPanel();
				add(colorPanel[i]);
				colorPanel[i].addMouseListener(myMouse);
				colorPanel[i].setBackground(colors[i]);

			} else if (i > 9 && i < 13) {
				if(j != 4) {
					brushLabel[j] = new JLabel(new ImageIcon("img/brushes" + j + ".png"));
					add(brushLabel[j]);
					brushLabel[j].addMouseListener(myMouse);
					
				}
				j++;

			} else if (i >= 13 && i <= 22) {
				colorPanel[i] = new JPanel();
				add(colorPanel[i]);
				colorPanel[i].addMouseListener(myMouse);
				colorPanel[i].setBackground(colors[i]);

			} else if (i > 22) {
				if(j != 4) {
					brushLabel[j] = new JLabel(new ImageIcon("img/brushes" + j + ".png"));
					add(brushLabel[j]);
					brushLabel[j].addMouseListener(myMouse);
					
				}
				j++;

			}
		}
	}

	public void run() {
		while (true) {

			currentColor = client.colorStatus;
			brushThickness = client.brushThickness;

		}
	}

	private class MyMouse extends MouseAdapter {
		public void mousePressed(MouseEvent event) {
			if (ChatClient.isArtist) {
				for (int i = 0; i < panelSize; i++) {
					if (event.getSource() == colorPanel[i]) {
						status = i;
						client.sendMessages("7" + status + " thickness " + brushThickness);
						statusFill = status;
					}
				}
				if (event.getSource() == brushLabel[0]) {
					client.sendMessages("7" + status + " thickness " + 5);
					DrawingPanel.fill = false;
				}
				if (event.getSource() == brushLabel[1]) {
					client.sendMessages("7" + status + " thickness " + 15);
					DrawingPanel.fill = false;
				}
				if (event.getSource() == brushLabel[2]) {
					client.sendMessages("7" + status + " thickness " + 25);
					DrawingPanel.fill = false;
				}
				if (event.getSource() == brushLabel[3]) {
					DrawingPanel.clearPanel();
					client.sendMessages("7" + "clear");
					DrawingPanel.fill = false;
				}
				if (event.getSource() == brushLabel[4]) {
					client.sendMessages("7" + "delete " + statusFill);
					DrawingPanel.g.setColor(colors[statusFill]);
					
				}
				if (event.getSource() == brushLabel[5]) {
					
					client.sendMessages("7" + "fill " + statusFill);
					DrawingPanel.fillPanel(colors[statusFill]);
					DrawingPanel.fill = true;
				}
			}

		}
	}

}