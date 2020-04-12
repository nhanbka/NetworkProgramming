
import java.io.*;
import java.net.*;

public class MultiThreadServer {
	
	private static int tcp_port = 81; // configure port for Server
	
	public static void main(String argv[]) {
		
		try {
			ServerSocket listenSocket = new ServerSocket(tcp_port); // open listen port
			System.out.println("Web server is listening at port " + tcp_port);
			while(true) {
				Socket connectionSocket = listenSocket.accept();
				MultiThreadServerHandle thread = new MultiThreadServerHandle(connectionSocket);
				thread.start();
			}
		} catch (BindException b) {
			System.out.println("You got the BindException with port: " + tcp_port);
		} catch (IOException i) {
			i.printStackTrace();
		} catch (OutOfMemoryError o) {
			o.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
		
}
