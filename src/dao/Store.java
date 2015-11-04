package dao;


import java.io.DataInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


import conf.Config;

public class Store {

	public static void DataStreamReceiver() {
		Socket	clientSocket = null;
		InputStream is = null;
		DataInputStream dis = null;
		ServerSocket listener = null;
		try {
			System.out.println("dsr");
			listener = new ServerSocket(Integer.parseInt(Config.config().getProperty("socket_port")));
			clientSocket = listener.accept();

			//clientSocket.setSoTimeout(Integer.parseInt(Config.config().getProperty("socket_timeout")));
			clientSocket.setKeepAlive(true);
			is = clientSocket.getInputStream();
			dis = new DataInputStream(is);
			String line;
			while ((line = dis.readLine()) != null) {
				if(!line.equals("EOF")){
					System.out.println(line);
					//mongocode object code here
				}else{
					dis.close();
					is.close();
					clientSocket.close();
					listener.close();
					break;
				}
			}


		} 
		catch (Exception e) {

			e.printStackTrace();

		}

	}

}
