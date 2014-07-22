package timoschwarz.snake.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import timoschwarz.snake.model.Player;

public class LANController extends GameController
{
	private ServerSocket server;
	private static final int PORT = 1337;
	private Socket connectionToTheServer;
	private Socket connectionToClient;
	private Player host;
	private Player client;

	private LANConfigurator configurator;

	public LANController(String namePlayerOne, String namePlayerTwo)
	{
		super(namePlayerOne, namePlayerTwo);
	}

	@Override
	protected void prepareStartOfGame(String namePlayerOne, String namePlayerTwo)
	{
		configurator = new LANConfigurator();
	}

	private void establishConnectionToServer()
	{
		try
		{
			connectionToClient = server.accept();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createServer(int port)
	{
		try
		{
			server = new ServerSocket(port);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void createSocketToServer(InetAddress address, int port)
	{
		try
		{
			connectionToTheServer = new Socket(address, port);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
