package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;

import timoschwarz.snake.model.Player;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;
import timoschwarz.snake.model.SnakePieceType;
import timoschwarz.snake.view.Entity;
import timoschwarz.snake.view.Playground;
import timoschwarz.util.KeyBindings;

public class Controller
{
	public static final int SNAKE_SIZE = 15;
	public static final int PAINT_SIZE = 15;
	public static final int PLAYGROUND_SIZE_X = 500;
	public static final int PLAYGROUND_SIZE_Y = 500;
	private static final int AMOUNT_OF_LOOSE_SNAKEPIECES = 1;

	private Playground playground;
	private JFrame frame;
	private JButton startButton;

	private Player playerOne;
	private Player playerTwo;

	private ArrayList<Entity> looseSnakePieces;

	public Controller()
	{

		this.setPlayerOne(new Player("Player One"));
		this.setPlayerTwo(new Player("Player Two"));
		this.frame = new JFrame("COMBAT SNAKEZ!!!111");
		setLooseSnakePieces(new ArrayList<Entity>());

		Snake snakeOne = new Snake(SNAKE_SIZE, SNAKE_SIZE, 0);
		Snake snakeTwo = new Snake(SNAKE_SIZE, SNAKE_SIZE, 10);

		playerOne.setSnake(snakeOne);
		playerTwo.setSnake(snakeTwo);

		LinkedList<Snake> snakes = new LinkedList<Snake>();
		snakes.add(snakeOne);
		snakes.add(snakeTwo);

		this.playground = new Playground(this, PLAYGROUND_SIZE_X, PLAYGROUND_SIZE_Y);

		playground.addEntity(createEntity(snakeOne, "white"));
		playground.addEntity(createEntity(snakeTwo, "red"));

		KeyBindings keyBindings = new KeyBindings(playground, snakeOne, snakeTwo);
		configureFrame();
	}

	public void endGame()
	{
		stopSnakesAndPlayers();
	}

	private void stopSnakesAndPlayers()
	{
		playerOne.getSnake().setAlive(false);
		playerOne.setAlive(false);
		playerTwo.getSnake().setAlive(false);
		playerTwo.setAlive(false);
	}

	private Entity createEntity(Snake snakeOne, String color)
	{
		ArrayList<BufferedImage> imagesSnakeOne = new ArrayList<BufferedImage>();
		ArrayList<Long> timingsSnakeOne = new ArrayList<Long>();
		imagesSnakeOne.add(createColouredImage(color, PAINT_SIZE, PAINT_SIZE, false));
		timingsSnakeOne.add(500l);

		return new Entity(imagesSnakeOne, timingsSnakeOne, snakeOne);
	}

	private void showPlayground()
	{
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

	public void moveSnakes()
	{
		playerOne.getSnake().move();
		playerTwo.getSnake().move();
	}

	private void configureFrame()
	{
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				playground.running.set(true);
				showPlayground();
				startSnakes();
			}
		});
		startButton.setVisible(true);

		Dimension size = playground.getSize();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(size.height, size.width);
		frame.setLayout(new BorderLayout());
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.add(playground, BorderLayout.CENTER);
		frame.add(startButton, BorderLayout.PAGE_END);
		frame.setVisible(true);
	}

	public void checkSnakes()
	{
		playerOne.getSnake();
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

	public void startSnakes()
	{
		initLooseSnakePieces();
	}

	private void initLooseSnakePieces()
	{
		looseSnakePieces = new ArrayList<>();
		for (int i = 0; i < AMOUNT_OF_LOOSE_SNAKEPIECES; i++)
		{
			Snake snake = new Snake(1, 6, 6);
			Entity entity = createEntity(snake, "blue");
			createNewLooseSnakePieceForEntity(entity);
			looseSnakePieces.add(entity);
		}
	}

	private void createNewLooseSnakePieceForEntity(Entity entity)
	{
		Random random = new Random();

		int randomX = 0;
		int randomY = 0;
		do
		{
			randomX = random.nextInt(PLAYGROUND_SIZE_X + 1);
			randomY = random.nextInt(PLAYGROUND_SIZE_Y + 1);
		}
		while (coordinatesAreFree(randomX, randomY));

		LinkedList<SnakePiece> pieces = new LinkedList<SnakePiece>();
		pieces.add(new SnakePiece(randomX, randomY, SnakePieceType.TAIL));
		entity.getSnake().setPieces(pieces);

	}

	private boolean coordinatesAreFree(int randomX, int randomY)
	{
		if (playerOne.getSnake().snakeBlocksCoordinates(randomX, randomY)
			|| playerTwo.getSnake().snakeBlocksCoordinates(randomX, randomY))
		{
			return false;
		}
		return true;
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

	public void removeLooseSnakePiece(Entity entity)
	{
		createNewLooseSnakePieceForEntity(entity);
	}

	public ArrayList<Entity> getLooseSnakePieces()
	{
		return looseSnakePieces;
	}

	public void setLooseSnakePieces(ArrayList<Entity> looseSnakePieces)
	{
		this.looseSnakePieces = looseSnakePieces;
	}
}
