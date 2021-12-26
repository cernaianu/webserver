package testing;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

import graphics.GUI;
import webserver.WebServer;
import webserver.WebServer.ServerState;

public class WebServerTest {

	WebServer webServer;
	GUI gui;
	@Before
	public void setUp() throws Exception {
		gui = new GUI();
		gui.startApplication();
		
		webServer = new WebServer(gui);
	}

	@Test
	public void testRun() {
		
	}

	@Test
	public void testSetPortNumber() {
		webServer.setPortNumber(3000);
		assertEquals("Port number should be equal:", webServer.getPortNumber(), 3000);
		assertNotEquals("Port number should not be equal:", webServer.getPortNumber(), 5666);
	}

	@Test
	public void testSetServerState() {
		webServer.setServerState(ServerState.STOPPED);
		assertEquals("Server state should be equal:", webServer.getServerState(), ServerState.STOPPED);
		assertNotEquals("Server state should not be equal:", webServer.getServerState(), ServerState.CONNECTED);
	}

	@Test(expected = IOException.class)
	public void testStartUpServer()  throws IOException{
		ServerSocket serverSocket = new ServerSocket(-1);
		String addressTest = serverSocket.getInetAddress().toString();
		serverSocket = new ServerSocket(1000);
		
		ServerState serverStateTest = ServerState.CONNECTED;
		Socket clientSocket = new Socket(webServer.getAddress(), webServer.getPortNumber());
		WebServer webServerTest = new WebServer(clientSocket, webServer.getAddress(), webServer.getRootDirectory(), webServer.getMaintenanceDirectory(), webServer.getPortNumber(), webServer.getServerState());
		
		
	}

	@Test
	public void testCloseServer() {
		fail("Not yet implemented"); // TODO
	}


	@Test
	public void testGetAddress() {
		webServer.setAddress("0.0.0.0");
		assertEquals("Address should check out",webServer.getAddress(),"0.0.0.0");
		assertNotEquals("Address should not check out",webServer.getAddress(),"0.1.0.0");
	}


	@Test
	public void testValidateRoot() {
		webServer.setRootDirectory("www");
		assertEquals(webServer.getRootDirectory(),"www");
		assertNotEquals(webServer.getRootDirectory(),"wwww");
		assertEquals(webServer.validateRoot(), true);
		webServer.setRootDirectory("rrr");
		assertEquals(webServer.validateRoot(), false);
	}

	@Test
	public void testValidateMaintenance() {
		webServer.setMaintenanceDirectory("maintenance");
		assertEquals(webServer.getMaintenanceDirectory(),"maintenance");
		assertNotEquals(webServer.getMaintenanceDirectory(),"wwww");
		assertEquals(webServer.validateMaintenance(), true);
		webServer.setMaintenanceDirectory("rrr");
		assertEquals(webServer.validateMaintenance(), false);
	}

}
