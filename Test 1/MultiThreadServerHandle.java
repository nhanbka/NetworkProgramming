import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class MultiThreadServerHandle extends Thread {

	private Socket connectionSocket;
	
	public MultiThreadServerHandle(Socket s) {
		this.connectionSocket = s;
	}
	
	public void run() {
		InputStreamReader inputStream;
		BufferedReader input;
		try {
			System.out.println("- Got request from: " + connectionSocket.getInetAddress().getHostAddress() + "\n\n");
			inputStream = new InputStreamReader(connectionSocket.getInputStream());
			input = new BufferedReader(inputStream);
			DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());

			String requestMessageLine = input.readLine();
			if (requestMessageLine != null) {
				System.out.println("------Input Stream--------\n");
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
						output.writeBytes("\r\n"); // Mark the end of HTTP header
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
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}


}
