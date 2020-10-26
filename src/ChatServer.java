import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.net.InetAddress;

public class ChatServer extends Thread {

	private static final int PORT = 4444;
	private static HashSet<String> names = new HashSet<String>();
	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	public static InetAddress ip;
	public static String ip2;
	public static int clientCtr;

	public ChatServer() {
		System.out.println("The chat server is running.");

		try {
			ip = InetAddress.getLocalHost();
			String temp = "" + ip;
			String arr[] = temp.split("/");
			ip2 = arr[1];

			System.out.println(ip2);
			clientCtr = 0;
		} catch (IOException e) {
		}

	}

	public void run() {
		try {
			ServerSocket listener = new ServerSocket(PORT);
			try {
				while (true) {
					
					new Handler(listener.accept()).start();
				}
			} finally {
				listener.close();
			}
		} catch (Exception e) {
		}
	}

	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		public Handler(Socket socket) {
			this.socket = socket;
			System.out.println("Client " + ++clientCtr);
		}

		public void run() {

			try {

				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					System.out.println("HAHA");
					out.println("IDENTIFIER " + clientCtr);
					// out.println("MAX " + clientCtr);

					out.println("SUBMITNAME");
					name = in.readLine();
										

					if (name == null) {
						return;
					}
					synchronized (names) {
						if (!names.contains(name)) {

							
							
							names.add(name);
							
							break;
						} else {
							break;
						}
					}
				}

				out.println("NAMEACCEPTED");
				writers.add(out);

				while (true) {
					String input = in.readLine();
					String identifier = "";

					if (input == null) {
						return;
					}
					if (input.charAt(0) == '1') {
						identifier = "MESSAGE ";
					} else if (input.charAt(0) == '2') {
						identifier = "AVATAR ";
					} else if (input.charAt(0) == '3') {
						identifier = "POINTS ";
					} else if (input.charAt(0) == '4') {
						identifier = "WORD ";
					} else if (input.charAt(0) == '5') {
						identifier = "TIME ";
					} else if (input.charAt(0) == '6') {
						identifier = "TURN ";
					} else if (input.charAt(0) == '7') {
						identifier = "COLORPALETTE ";
					} else if (input.charAt(0) == '8') {
						identifier = "COUNTDOWN ";
					} else if (input.charAt(0) == '9') {
						identifier = "CONTINUE ";
					}else if (input.charAt(0) == '0') {
						identifier = "DISCONNECT ";
					}else if (input.charAt(0) == 'a') {
						identifier = "SCORE ";
					}

					for (PrintWriter writer : writers) {
						writer.println(identifier + name + ": " + input.substring(1));
						writer.println("MAX " + clientCtr);
//						writer.println("AVATAR " + name + ": " + input);
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (name != null) {
					names.remove(name);
				}
				if (out != null) {
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
					// System.out.print("HAHAHA");
				}
			}
		}
	}
}
