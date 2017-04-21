import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectThread implements Runnable{
	private Socket clientSocket;
	private Socket serverSocket;
	private ObjectInputStream client_reader;
	private ObjectInputStream server_reader;
	private ObjectOutputStream client_writer;
	private ObjectOutputStream server_writer;
	
	public ConnectThread(Socket cs){
		clientSocket = cs;
		try{
			client_writer = new ObjectOutputStream(clientSocket.getOutputStream());
			client_reader = new ObjectInputStream(clientSocket.getInputStream()); 			  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	@Override
	public void run() {
		boolean isRun = true;
		while(isRun){						
			try{
				Message message = receiveClient();
				String type = message.type;
				switch(type){
					case "connect" :		// 家南 积己 棺 楷搬
						connectServer(message.dst_addr,message.dst_port);
						break;
					case "data" :			// data甫 傈价
						sendServer(message);
						sendClient(receiveServer());
						break;
					case "disconnect" :		// 家南 昏力
						disconnectServer();
						isRun = false;
						break;
					default :					
						break;
				}
			}catch(Exception e){				
				e.printStackTrace();
				break;
			}			
		}
		System.out.println("thread terminated");
	}
	
	public void connectServer(String h, int p) throws Exception{
		String host = h;
		int port = p;	
		System.out.println("host:"+host+"/port:"+port);
		serverSocket = new Socket(host,port);
		server_writer = new ObjectOutputStream(serverSocket.getOutputStream());
		server_reader = new ObjectInputStream(serverSocket.getInputStream());		
		System.out.println("Client-Server connect!");
	}
	
	public void disconnectServer() throws Exception{
		server_writer.close();	
		server_reader.close();
		serverSocket.close();
		System.out.println("Client-Server disconnect!");
	}
	
	public void sendServer(Message message) throws Exception{
		server_writer.reset();
		server_writer.writeObject(message);
		if(message.msg != null)
			System.out.println("Gateway->Server send message ["+message.msg+"]");
	}
	
	public Message receiveServer() throws Exception{
		Message messagesg = (Message)server_reader.readObject();
		System.out.println("Server->Gateway send message ["+messagesg.msg+"]");
		return messagesg;
	}
	
	public void sendClient(Message message)throws Exception{
		client_writer.reset();
		client_writer.writeObject(message);
		if(message.msg != null)
			System.out.println("Gateway->Server send message ["+message.msg+"]");
	}
	
	public Message receiveClient() throws Exception{
		Message messagesg = (Message)client_reader.readObject();
		System.out.println("Client->Gateway send message ["+messagesg.msg+"]");
		return messagesg;
	}
}
