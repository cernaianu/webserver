package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import graphics.GUI;

public class WebServer extends Thread {
	protected Socket clientSocket;

	

	private volatile ServerSocket serverSocket = null;

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	GUI graphicalInterface;

	public enum ServerState {
		STOPPED,
		CONNECTED,
		MAINTENANCE
	}

	private volatile ServerState serverState;

	private int portNumber=2400;

	private String address;

	private volatile String rootDirectory;

	private String maintenanceDirectory;

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public ServerState getServerState() {
		return serverState;
	}

	public void setServerState(ServerState serverState) {
		this.serverState = serverState;
		graphicalInterface.switchState(serverState);
	}

	public WebServer(GUI gui) {

		graphicalInterface = gui;
		setServerState(ServerState.STOPPED);

	}

	public void startUpServer()
	{
		try
		{
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Connection Socket Created");
			address = serverSocket.getInetAddress().toString();
			setServerState(ServerState.CONNECTED);
			try
			{
				while(true)
				{
					System.out.println("Waiting for Connection");
					
					new WebServer(serverSocket.accept(), this.address,this.rootDirectory, this.maintenanceDirectory, this.portNumber, this.serverState);
				}
			}
			catch (IOException e)
			{
				System.out.println("Accept failed.");
				//System.exit(1);
			}
		}
		catch(IOException e)
		{
			System.err.println("Could not listen on port");
			System.exit(1);
		}
		finally
		{
			try
			{
				serverSocket.close();
			}
			catch (IOException e)
			{
				System.err.println("Could not close port");
				System.exit(1);
			}
		}
	}

	public void closeServer()
	{
		try {
			setServerState(ServerState.STOPPED);
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WebServer(Socket ClientSoc, String address, String rootDirectory, String maintenanceDirectory, int portNumber, ServerState serverState)
	{
		this.address = address;
		this.rootDirectory = rootDirectory;
		this.maintenanceDirectory = maintenanceDirectory;
		this.portNumber = portNumber;
		this.serverState = serverState;
		clientSocket = ClientSoc;
		start();
	}

	@Override
	public void run() {

		System.out.println("New Communication Thread Started");

		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));


			String inputLine;

			StringBuilder requestBuilder = new StringBuilder();

			String request;

			while ((inputLine = in.readLine()) != null) {
				//System.out.println("Server: " + inputLine);
				//out.println(inputLine);
				requestBuilder.append(inputLine + "\r\n");
				if (inputLine.trim().equals(""))
					break;
			}
			request = requestBuilder.toString();

			try {
			String[] requestsLines = request.split("\r\n");
			String[] requestLine = requestsLines[0].split(" ");
			String method = requestLine[0];
			String path = requestLine[1];
			String version = requestLine[2];
			String host = requestsLines[1].split(" ")[1];

			List<String> headers = new ArrayList<>();
			for (int h = 2; h < requestsLines.length; h++) {
				String header = requestsLines[h];
				headers.add(header);
			}
			
			

			String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
					clientSocket.toString(), method, path, version, host, headers.toString());
			System.out.println(accessLog);

			Path filePath;
			String fixedPath = path.replaceAll("%20", " ");
			if(serverState == ServerState.MAINTENANCE)
			{
				filePath = getFilePath("", maintenanceDirectory);
			}
			else
			{
				filePath = getFilePath(fixedPath, rootDirectory);
			}



			if (Files.exists(filePath)) {
				// file exist
				String contentType = guessContentType(filePath);
				sendResponse(clientSocket, "200 OK", contentType, Files.readAllBytes(filePath));
			} else {
				// 404
				byte[] notFoundContent = "<h1>Not found :(</h1>".getBytes();
				sendResponse(clientSocket, "404 Not Found", "text/html", notFoundContent);
			}

			out.write(request);
			out.close();
			in.close();
			clientSocket.close();
			}
			catch(ArrayIndexOutOfBoundsException exception)
			{
				out.close();
				in.close();
				clientSocket.close();
			}
		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(1);
		}
	}



	private static void sendResponse(Socket client, String status, String contentType, byte[] content) throws IOException {
		OutputStream clientOutput = client.getOutputStream();
		clientOutput.write(("HTTP/1.1 \r\n" + status).getBytes());
		clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
		clientOutput.write("\r\n".getBytes());
		clientOutput.write(content);
		clientOutput.write("\r\n\r\n".getBytes());
		clientOutput.flush();
		client.close();
	}

	private static String guessContentType(Path filePath) throws IOException {
		return Files.probeContentType(filePath);
	}

	private Path getFilePath(String path, String directory) {
		if ("/".equals(path)) {
			path = "/index.html";
		}

		if(serverState == ServerState.MAINTENANCE)
			path = "/maintenance.html";

		return Paths.get(directory, path);
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getRootDirectory() {
		return rootDirectory;
	}



	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}



	public String getMaintenanceDirectory() {
		return maintenanceDirectory;
	}



	public void setMaintenanceDirectory(String maintenanceDirectory) {
		this.maintenanceDirectory = maintenanceDirectory;
	}

	public boolean validateRoot()
	{
		if(Files.exists(Paths.get(rootDirectory,"index.html")) && !rootDirectory.equals(""))
			return true;
		return false;
	}

	public boolean validateMaintenance()
	{
		if(Files.exists(Paths.get(maintenanceDirectory,"maintenance.html")) && !maintenanceDirectory.equals(""))
			return true;
		return false;
	}

	public void startUpServerInNewThread()
	{
		 
		 Thread temp = new Thread(new Runnable() {

			@Override
			public void run() {
				startUpServer();
			}
			
		});
		 System.out.println(getAddress() + " on thread: " + Thread.currentThread().getName());
		temp.start();
		try {
			
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(getAddress() + " on thread: " + Thread.currentThread().getName());
		
	}
	
	

}