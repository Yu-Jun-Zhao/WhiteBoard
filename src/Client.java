import java.net.*;
import java.beans.XMLDecoder;
import java.io.*;
import java.util.*;

public class Client implements Runnable{
	protected Socket socket;
	private Canvas canvas;
	private String host;
	private int port;
	
	public Client(String host, int port, Canvas canvas){
		try {
			this.canvas = canvas;
			this.host = host;
			this.port = port;
			socket = new Socket(host,port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void run(){
		// TODO Auto-generated method stub
		Pkg pkg = null;
		XMLDecoder decoder = null;
		ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			System.out.println("Connected");

			
			while(true) {
				
				//Pkg pkg = (Pkg)decoder.readObject();
				String xmlString = (String)inputStream.readObject();
				if(xmlString != null){
					decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
					pkg = (Pkg) decoder.readObject();
					
				
					if(pkg !=null){
						String command = pkg.getCommand();
						DShapeModel model = pkg.getModel();
						if(command.equals("ADD")){
							canvas.addShape(model);
						}else if(command.equals("REMOVE") ){
							canvas.removeByModel(model);
						}
						else if(command.equals("FRONT")){
							canvas.moveToFrontbyId(model);
						}
						else if(command.equals("BACK")){
							canvas.moveToBackbyId(model);
						}
						else if(command.equals("CHANGE")){
							canvas.movebyId(model);
						}
					}
				}
				
			
				
					
			}
		}catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound");
			e.printStackTrace();
		}finally{
			if(decoder != null)decoder.close();
			if(inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
	
	
