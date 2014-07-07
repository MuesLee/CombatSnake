package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import timoschwarz.snake.model.Player;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.view.Entity;
import timoschwarz.snake.view.Playground;
import timoschwarz.util.KeyBindings;

public class Controller implements Observer
{
	private Playground playground;
	private JFrame frame;

	private Referee referee;

	private Player playerOne;
	private Player playerTwo;

	public Controller()
	{

		this.setPlayerOne(new Player("Player One"));
		this.setPlayerTwo(new Player("Player Two"));
		this.frame = new JFrame("COMBAT SNAKEZ!!!111");

		Snake snakeOne = new Snake(15);
		Snake snakeTwo = new Snake(15);

		playerOne.setSnake(snakeOne);
		playerTwo.setSnake(snakeTwo);

		LinkedList<Snake> snakes = new LinkedList<Snake>();
		snakes.add(snakeOne);
		snakes.add(snakeTwo);

		this.playground = new Playground(500, 500);

		ArrayList<BufferedImage> imagesSnakeOne = new ArrayList<BufferedImage>();
		ArrayList<Long> timingsSnakeOne = new ArrayList<Long>();
		imagesSnakeOne.add(createColouredImage("white", 5, 5, false));
		timingsSnakeOne.add(500l);

		ArrayList<BufferedImage> imagesSnakeTwo = new ArrayList<BufferedImage>();
		ArrayList<Long> timingsSnakeTwo = new ArrayList<Long>();
		imagesSnakeTwo.add(createColouredImage("red", 5, 5, false));
		timingsSnakeTwo.add(500l);

		playground.addEntity(new Entity(imagesSnakeOne, timingsSnakeOne, snakeOne));
		playground.addEntity(new Entity(imagesSnakeTwo, timingsSnakeTwo, snakeTwo));

		this.setReferee(new Referee(playground, snakes));
		referee.addObserver(this);
		KeyBindings keyBindings = new KeyBindings(playground, snakeOne, snakeTwo);
	}

	public void showPlayground()
	{
		configureFrame();
		Thread loop = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				playground.gameLoop();
			}
		});
		loop.start();
	}

	private void configureFrame()
	{
		Dimension size = playground.getSize();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(size.height, size.width);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
		frame.add(playground, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	public void checkSnakes()
	{
		playerOne.getSnake();
	}

	private void startConsumeProcess()
	{
		// TODO: Punkte erhöhen
	}

	public void setPlaygroundSize(Dimension size)
	{
		playground.setSize(size);
	}

	public Player getPlayerOne()
	{
		return playerOne;
	}

	public void setPlayerOne(Player playerOne)
	{
		this.playerOne = playerOne;
	}

	public Player getPlayerTwo()
	{
		return playerTwo;
	}

	public void setPlayerTwo(Player playerTwo)
	{
		this.playerTwo = playerTwo;
	}

	private void startSnakeOne()
	{
		Snake snake = playerOne.getSnake();
		Thread snakeThread = new Thread(snake);
		snakeThread.start();
	}

	public void startSnakes()
	{
		startSnakeOne();
		startSnakeTwo();
		startReferee();
	}

	private void startReferee()
	{
		Thread refereeThread = new Thread(referee);
		refereeThread.start();
	}

	private void startSnakeTwo()
	{
		Snake snake = playerTwo.getSnake();
		Thread snakeThread = new Thread(snake);
		snakeThread.start();

	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		if (arg0 instanceof Referee && arg1 instanceof Snake)
		{
			Snake loser = (Snake) arg1;

			if (playerOne.getSnake() == loser)
			{
				System.out.println(playerTwo.getName() + " won!");
			}
			else
			{
				System.out.println(playerOne.getName() + " won!");
			}

			playerOne.getSnake().setAlive(false);
			playerTwo.getSnake().setAlive(false);
			referee.setActive(false);

		}
	}

	public Referee getReferee()
	{
		return referee;
	}

	public void setReferee(Referee referee)
	{
		this.referee = referee;
	}

	public static BufferedImage createColouredImage(String color, int w, int h, boolean circular)
	{
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		switch (color.toLowerCase())
		{
			case "green":
				g2.setColor(Color.GREEN);
			break;
			case "magenta":
				g2.setColor(Color.MAGENTA);
			break;
			case "red":
				g2.setColor(Color.RED);
			break;
			case "yellow":
				g2.setColor(Color.YELLOW);
			break;
			case "blue":
				g2.setColor(Color.BLUE);
			break;
			case "orange":
				g2.setColor(Color.ORANGE);
			break;
			case "cyan":
				g2.setColor(Color.CYAN);
			break;
			case "gray":
				g2.setColor(Color.GRAY);
			break;
			default:
				g2.setColor(Color.WHITE);
			break;
		}
		if (!circular)
		{
			g2.fillRect(0, 0, img.getWidth(), img.getHeight());
		}
		else
		{
			g2.fillOval(0, 0, img.getWidth(), img.getHeight());
		}
		g2.dispose();
		return img;
	}
}
