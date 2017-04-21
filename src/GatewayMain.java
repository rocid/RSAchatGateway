import java.net.ServerSocket;
import java.net.Socket;

public class GatewayMain {
	
	public static void main(String[] args) throws Exception{
		int port = 3000;		
		ServerSocket networkInterface;		
		networkInterface = new ServerSocket(port);
		
		System.out.println("Gateway run!");
		
		while(true){
			Socket socket = networkInterface.accept();
			System.out.println("connect Client");
			
			ConnectThread conn = new ConnectThread(socket);
			Thread t = new Thread(conn);
			t.start();
		}
	}

}
