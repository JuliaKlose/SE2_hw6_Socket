import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BasicServer {
	public static void main(String[] args) {
		if (args.length != 1) { // Test for correct num. of arguments
			System.err.println("ERROR server port number not given");
			System.exit(1);
		}
		int port_num = Integer.parseInt(args[0]);
		ServerSocket rv_sock = null;
		try {
			rv_sock = new ServerSocket(port_num);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		while (true) { // run forever, waiting for clients to connect
			System.out.println("\nWaiting for client to connect...");
			try {
				Socket s_sock = rv_sock.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(s_sock.getInputStream()));
				PrintWriter out = new PrintWriter(new OutputStreamWriter(s_sock.getOutputStream()), true);
				String command = in.readLine();
				if (command != null) {
					// System.out.println("Client's message: " + command);
					// out.println("I got your message");
					String[] comArr = command.split(" ", 2);
					switch (comArr[0]) {
					case "GET":
						String filename = comArr[1].substring(1, (comArr[1].length()) - 1);
						String line = null;
						String text = "";
						try {
							BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
							while ((line = bufferedReader.readLine()) != null) {
								text += (line + "\n");
							}
							System.out.println("Get the file successfully:\n"+ text);
							out.println(text);
							bufferedReader.close();
						} catch (FileNotFoundException ex) {
							System.out.println("ERROR: no such file");
							out.println("ERROR: no such file");
						} catch (IOException ex) {
							System.out.println("Error reading file");
							out.println("Error reading file ");
						}
						break;
					case "BOUNCE":
						System.out.println("Bounce the text successfully:\n"+comArr[1].substring(1, (comArr[1].length()) - 1));
						out.println(comArr[1].substring(1, (comArr[1].length()) - 1));
						break;
					case "EXIT":
						if (comArr.length == 1) {
							System.out.println("Exit Successfully!");
							// out.println("Exit Successfully!");
						} else {
							System.out.println("Exit code has been returned:\n"+ comArr[1].substring(1, (comArr[1].length()) - 1));
							out.println(comArr[1].substring(1, (comArr[1].length()) - 1));
						}
						break;
					}
				}
				s_sock.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}