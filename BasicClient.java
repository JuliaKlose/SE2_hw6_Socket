import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class BasicClient {
	public static void main(String[] args) {
		if (args.length != 2) { // Test for correct num. of arguments
			System.err.println("ERROR server host name AND port number not given");
			System.exit(1);
		}
		int port_num = Integer.parseInt(args[1]);
		boolean running_sign = true;
		while (running_sign) {
			try {
				BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("\nUser, enter your message: ");
				String command = userEntry.readLine();
				String[] comArr = command.split(" ", 2);
				switch (comArr[0]) {
				case "GET":
				case "BOUNCE":
				case "EXIT":
					if (comArr.length == 2 && comArr[1].length() > 0 && comArr[1].charAt(0) == '<'
							&& comArr[1].charAt(comArr[1].length() - 1) == '>'
							|| (comArr[0].equals("EXIT") && comArr.length == 1)) {
						Socket c_sock = new Socket(args[0], port_num);
						BufferedReader in = new BufferedReader(new InputStreamReader(c_sock.getInputStream()));
						PrintWriter out = new PrintWriter(new OutputStreamWriter(c_sock.getOutputStream()), true);
						out.println(command);
						out.flush();
						String line = null;
						String text ="";
						while((line = in.readLine()) != null){
							text +=(line +"\n");
						}
						System.out.print("Returned message:\n" + text);
						if (comArr[0].equals("EXIT")) {
							running_sign = false;
							System.out.println("Exit successfully!");
							break;
						}
						c_sock.close();
						break;
					}
				default:
					System.out.println(
							"WARNING: Wrong command format! \n The proper input should be like \n'Get <filename>', 'BOUNCE <Text-to-bounce>','EXIT' or 'EXIT <exit code>'");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				running_sign = false;
			}
		}
		System.exit(0);
	}
}