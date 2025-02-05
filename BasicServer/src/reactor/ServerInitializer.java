package reactor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


public class ServerInitializer {

	
	
	public static void main(String[] args){
		int port = 5000;
		System.out.println("Server ON :" + port);
		
		Reactor reactor = new Reactor(port);
		
		try{
			Serializer serializer = new Persister();
			File source = new File("HandlerList.xml");
			ServerListData serverList = serializer.read(ServerListData.class, source);
			
			for(HandlerListData handlerListData : serverList.getServer()){
				if(handlerListData.getName().equals("server1")){
					List<HandlerData> handlerList = handlerListData.getHandler();
					
					for(HandlerData handler : handlerList){
						try{
							reactor.registerHandler(handler.getHeader(), (EventHandler) Class.forName(handler.getHandler()).newInstance());
						} catch(InstantiationException e){
							e.printStackTrace();
						} catch(IllegalAccessException e){
							e.printStackTrace();
						} catch(ClassNotFoundException e){
							e.printStackTrace();
						} 
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		reactor.startServer();

	}
}
