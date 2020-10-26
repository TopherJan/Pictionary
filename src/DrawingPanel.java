import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel implements Runnable {

	protected static final int WIDTH = 1366;
	protected static final int HEIGHT = 768;
	protected volatile Thread thread;

	public static Color IMG_COLOR = Color.BLACK;
	public static Stroke IMG_STROKE = new BasicStroke(5f);
	public static boolean fill = false;

	private BufferedImage image;
	static Graphics2D g;

	private JTextField test = new JTextField(15);
	private List<Point> currentPts = new ArrayList<>();
	private MyMouse myMouse = new MyMouse();
	private ChatClient client;
	static int x1;
	static int y1;
	static int x2;
	static int y2;
	// protected volatile Thread thread;
	protected static boolean running;
	protected static volatile boolean displayMenu = false;
	// private BufferedImage image;
	// private Graphics2D g;
	private int FPS = 60;
	private int targetTime = 1000 / FPS;

	static Graphics2D g3;
	static JPanel drawPanel;

	public DrawingPanel(ChatClient client) {

		this.client = client;
		addMouseListener(myMouse);
		addMouseMotionListener(myMouse);
		setBounds(240, 92, 805, 520);

	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run() {
		init();

		long startTime;
		long urdTime;
		long waitTime;

		while (running) {

			startTime = System.nanoTime();
			urdTime = (System.nanoTime() - startTime) / 1000000;
			waitTime = targetTime - urdTime;

			try {
				Thread.sleep(waitTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			update();
			render();
			draw();
		}
	}

	public void update() {
		IMG_COLOR = ColorPalettePanel.colors[ColorPalettePanel.currentColor];
		IMG_STROKE = new BasicStroke(ColorPalettePanel.brushThickness);

	}

	public void render() {

		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	public void draw() {
		g3 = image.createGraphics();
		g3.setColor(IMG_COLOR);
		g3.setStroke(IMG_STROKE);
		for (int i = 1; i < currentPts.size(); i++) {
			x1 = currentPts.get(i - 1).x;
			y1 = currentPts.get(i - 1).y;
			x2 = currentPts.get(i).x;
			y2 = currentPts.get(i).y;

			client.sendMessages("3x1 " + x1 + " y1 " + y1 + " x2 " + x2 + " y2 " + y2);
			// g3.drawLine(x1,y1,x2,y2);
		}
		// g3.drawLine(client.x1,client.y1, client.x2, client.y2);
	}

	private void init() {
		running = true;
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.getGraphics();
		Graphics h = image.getGraphics();
		g.setColor(new Color(245, 245, 245));
		g.fillRect(0, 0, 805, 520);

	}

	public static void clearPanel() {
		g.setColor(new Color(245, 245, 245));
		g.fillRect(0, 0, 805, 520);
	}
	
	public static void fillPanel(Color color) {
		g.setColor(color);
		g.fillRect(0, 0, 805, 520);
	}

	private class MyMouse extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			if (ChatClient.isArtist) {
				if (fill == true) {
					g.setColor(ColorPalettePanel.colors[ColorPalettePanel.currentColor]);
					g.fillRect(0, 0, 805, 520);
				} else {
					currentPts = new ArrayList<>();
					if (!currentPts.contains(e.getPoint())) {
						currentPts.add(e.getPoint());
					}
				}
			}

		}

		public void mouseDragged(MouseEvent e) {
			if (ChatClient.isArtist) {
				if (!currentPts.contains(e.getPoint())) {
					currentPts.add(e.getPoint());
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (ChatClient.isArtist) {
				if (!currentPts.contains(e.getPoint())) {
					currentPts.add(e.getPoint());
				}
				currentPts.clear();
			}
			// repaint();
		}
	}

}
