package webserver;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class WebServer extends Thread {
	protected Socket clientSocket;

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(10008);
			System.out.println("Connection Socket Created");
			try {
				while (true) {
					System.out.println("Waiting for Connection");
					new WebServer(serverSocket.accept());
				}
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port: 10008.");
			System.exit(1);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port: 10008.");
				System.exit(1);
			}
		}
	}

	private WebServer(Socket clientSoc) {
		clientSocket = clientSoc;
		start();
	}

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
		    
		    
		    String fixedPath = path.replaceAll("%20", " ");
		    Path filePath = getFilePath(fixedPath);
		    
		   
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
	
	private static Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "/a.html";
        }

        return Paths.get("www", path);
    }
	
	
	
}