package timoschwarz.snake.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import timoschwarz.snake.model.Player;
import timoschwarz.util.Direction;
import timoschwarz.util.OPCode;

public class LANController extends GameController
{
	private ServerSocket server;
	private static final int PORT = 1337;
	private Socket connectionToTheServer;
	private Socket connectionToClient;
	private Player host;
	private Player client;

	private LANConfigurator configurator;
	private BufferedReader in;
	private PrintWriter out;

	public LANController(String namePlayerOne, String namePlayerTwo)
	{
		super(namePlayerOne, "Victim");
	}

	@Override
	protected void prepareStartOfGame(String namePlayerOne, String namePlayerTwo)
	{
		configurator = new LANConfigurator(this);
	}

	public void startLANGame()
	{
		super.prepareStartOfGame(host.getName(), client.getName());
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
		if (address == null)
		{
			return;
		}

		try
		{
			connectionToTheServer = new Socket(address, port);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
		}
	}

	public void startListener()
	{
		Thread listener = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				listenToSocket();
			}

		});
		listener.start();
	}

	private void listenToSocket()
	{
		while (true)
		{
			try
			{
				String line = in.readLine();
				if (!line.isEmpty())
				{
					OPCode opcode = processOPCode(line);
				}

				//Send data back to client

			}
			catch (IOException e)
			{
				System.out.println("Read failed");
				System.exit(-1);
			}
		}

	}

	private OPCode processOPCode(String line)
	{
		OPCode code = null;
		return code;
	}

	private void setNewDirectionOfClientsSnake(Direction direction)
	{
		client.getSnake().setDirection(direction);
	}

	public void hostGame(String playerName)
	{
		LANLobby lobby = new LANLobby(this);
		host = new Player(playerName);
		setPlayerOne(host);
		lobby.addPlayer(host);

		createServer(PORT);

	}

	public void joinGame(String hostIP, String playerName)
	{
		client = new Player(playerName);
		setPlayerTwo(client);
		InetAddress address = null;
		try
		{
			address = InetAddress.getByName(hostIP);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}

		createSocketToServer(address, PORT);

		try
		{
			in = new BufferedReader(new InputStreamReader(connectionToTheServer.getInputStream()));
			out = new PrintWriter(connectionToTheServer.getOutputStream(), true);
		}
		catch (IOException e)
		{
			System.out.println("Read failed");
			System.exit(-1);
		}
	}

}
