import java.net.ServerSocket;
import java.util.*;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.*;

public class Server implements Runnable {
	protected ServerSocket mySocket;
	protected int myPort;
	protected Class HandlerType;
	
	//private static volatile String COMMAND;
	//private static volatile DShapeModel MODEL;

	private static HashMap<Integer, LinkedList<Pkg>> data = new HashMap<>();
	
	
	private static int THREADID;
	
	public Server(int port){
		myPort = port;
		try {
			mySocket = new ServerSocket(myPort);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen(){
		System.out.println("Server listening at port" + myPort);
		try{
			
			while(true){
				Socket echoSocket = mySocket.accept();
				data.put(THREADID, new LinkedList<Pkg>());
				new ServerThread(THREADID,echoSocket).start();
				THREADID++;
			}
		}
		catch(IOException ioe){
			System.err.println(ioe.getMessage());
		}
	}
	
	
	public static void update(String command, DShapeModel model){
		if(command != null && model != null){
			Pkg pkg = new Pkg();
			pkg.setCommand(command);
			pkg.setModel(model);
			for(int i = 0; i < data.size(); i++){
				// Add pkg to each thread
				data.get(i).add(pkg);
				
			}
		}
		
	}
	
	private class ServerThread extends Thread{
		
		private Socket sock;
		private int id; 
		public ServerThread(int id, Socket sock){
			this.id = id;
			this.sock = sock;
		}
		
		@Override
		public void run(){
			XMLEncoder encoder = null;
			ObjectOutputStream outputStream = null;
			OutputStream stream = null;
			
				try {
					outputStream = new ObjectOutputStream((sock.getOutputStream()));
					
					while(true){
						Pkg pkg = null;
						
						if(!data.get(this.id).isEmpty()){
							pkg = data.get(this.id).getFirst();
								
							data.get(this.id).removeFirst();
							
						}
						if(pkg != null){
							stream = new ByteArrayOutputStream();
							encoder = new XMLEncoder(stream);
							encoder.writeObject(pkg); // Decoder only generates the XML file. It does not "send".
							
							encoder.close();
							String xmlString = stream.toString();
							
							System.out.println(xmlString);
							
							// send the xmlString
							outputStream.writeObject(xmlString);
							outputStream.flush();
						}
						
					}

				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					
					if(encoder != null) encoder.close();
					if(outputStream != null)
						try {
							outputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					if(stream != null)
						try {
							stream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}

			
		}
	}
	
	
	
	
	@Override
	public void run() {
		this.listen();
	}

}
