
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;


public class singleThreadServer {

	private static int tcp_port = 81; // configure port for Server

	public static void main(String argv[]) {

		try {
			ServerSocket listenSocket = new ServerSocket(tcp_port); // open listen port
			System.out.println("Web server is listening at port " + tcp_port);
			while (true) {
				Socket connectionSocket = listenSocket.accept();
				InputStreamReader inputStream;
				BufferedReader input;

				do {
					// handle null request from browser

					if (connectionSocket.isConnected())
						connectionSocket.close();
					connectionSocket = listenSocket.accept();
					inputStream = new InputStreamReader(connectionSocket.getInputStream());
					input = new BufferedReader(inputStream);
				} while (input == null);

				System.out
						.println("- Got request from: " + connectionSocket.getInetAddress().getHostAddress() + "\n\n");
				System.out.println("------Input Stream--------\n");

				DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());

				String requestMessageLine = input.readLine();
				if (requestMessageLine != null) {
					System.out.println("- Got first line HEADER: " + requestMessageLine);

					StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);
					String typeToken = tokenizedLine.nextToken();
					if (typeToken.equals("GET") || typeToken.equals("get")) {

						// this section is to handle GET request

						String fileName = tokenizedLine.nextToken();
						System.out.println("- Got file name " + fileName);
						if (fileName.startsWith("/") == true)
							fileName = fileName.substring(1);

						try {
							File file = new File(fileName);
							int numOfBytes = (int) file.length();
							FileInputStream inFile = new FileInputStream(fileName);
							byte[] fileInBytes = new byte[numOfBytes];

							inFile.read(fileInBytes);
							output.writeBytes("HTTP/1.0 200 Document Follows\r\n");

							if (fileName.endsWith(".jpg"))
								output.writeBytes("Content Type: image/jpeg\r\n");
							if (fileName.endsWith(".gif"))
								output.writeBytes("Content Type: image/gif\r\n");

							output.writeBytes("Content	Length: " + numOfBytes + "\r\n");
							output.writeBytes("\r\n"); // Mark the end of HTTP file
							output.write(fileInBytes, 0, numOfBytes);
						} catch (Exception e) {
							output.writeBytes("HTTP/1.0 404 File not found\r\n");
							output.writeBytes("\r\n");
							output.writeBytes("Excepton in reading file: " + e.getMessage());
						}
					} else if (typeToken.equals("POST") || typeToken.equals("post")) {

						// this section is to handle POST request

						String action = tokenizedLine.nextToken();
						if (action.startsWith("/"))
							action = action.substring(1);
						System.out.println("This request is trying to " + action + " file");
						String s = null;

						// Read header
						while ((s = input.readLine()).length() != 0) {
							System.out.println(s);
						}

						// code to read the post payload data
						StringBuilder payload = new StringBuilder();
						while (input.ready()) {
							payload.append((char) input.read());
						}
						String fileName = payload.toString().substring(5);
						System.out.println(fileName);

						try {
							File file = new File(fileName);
							int numOfBytes = (int) file.length();
							FileInputStream inFile = new FileInputStream(fileName);
							byte[] fileInBytes = new byte[numOfBytes];

							inFile.read(fileInBytes);
							output.writeBytes("HTTP/1.0 200 \r\n");
							output.writeBytes("Content-Disposition: attachment; filename=" + fileName + "\r\n");

							String fileType = fileName.substring(fileName.lastIndexOf("."));
							output.writeBytes("Content-Type: " + fileType + "\r\n");
							
							output.writeBytes("Content-Length: " + numOfBytes + "\r\n");
							output.writeBytes("\r\n"); 						// Mark the end of HTTP header
							output.write(fileInBytes, 0, numOfBytes);
						} catch (Exception e) {
							output.writeBytes("HTTP/1.0 404 File not found\r\n");
							output.writeBytes("\r\n");
							output.writeBytes("Excepton in reading file: " + e.getMessage());
						} 

					} // end if request message is null

					// close connection
					connectionSocket.close();
				}

			} // End while(true)
		} catch (BindException b) {
			System.out.println("You got the BindException with port: " + tcp_port);
		} catch (IOException i) {
			i.printStackTrace();
		} catch (OutOfMemoryError o) {
			o.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} // end Big try
	} 

}
