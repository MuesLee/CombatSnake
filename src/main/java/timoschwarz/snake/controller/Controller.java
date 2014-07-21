package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import timoschwarz.snake.model.Player;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;
import timoschwarz.snake.model.SnakePieceType;
import timoschwarz.snake.model.World;
import timoschwarz.snake.view.Entity;
import timoschwarz.snake.view.Playground;
import timoschwarz.snake.view.SnakeEntity;
import timoschwarz.util.KeyBindings;

public class Controller
{
	public static final int SNAKE_SIZE = 15;
	public static final int PAINT_SIZE = 15;
	public static final int PLAYGROUND_SIZE_X = 500;
	public static final int PLAYGROUND_SIZE_Y = 500;
	private static final int AMOUNT_OF_LOOSE_SNAKEPIECES = 1;
	private static final int POINTS_FOR_CONSUMPTION = 10;
	private static final int GAME_SPEED = 100;
	private boolean gameIsActive = false;

	public static final String TEXT_SNAKE_ONE_WAS_VICTORIOUS = "Snake One has won!";
	public static final String TEXT_SNAKE_TWO_WAS_VICTORIOUS = "Snake Two has won!";
	public static final String TEXT_BOTH_SNAKES_DED = "BOS SNAIGS DED!!";

	private Playground playground;
	private JFrame frame;
	private JButton startButton;
	private JButton resetButton;

	private Player playerOne;
	private Player playerTwo;

	private World world;

	public Controller()
	{

		initGame();
	}

	private void initGame()
	{
		this.setPlayerOne(new Player("Player One"));
		this.setPlayerTwo(new Player("Player Two"));
		this.frame = new JFrame("COMBAT SNAKEZ!!!111");

		Snake snakeOne = new Snake(SNAKE_SIZE, SNAKE_SIZE, 0);
		Snake snakeTwo = new Snake(SNAKE_SIZE, SNAKE_SIZE, 10);

		playerOne.setSnake(snakeOne);
		playerTwo.setSnake(snakeTwo);

		LinkedList<Snake> snakes = new LinkedList<Snake>();
		snakes.add(snakeOne);
		snakes.add(snakeTwo);

		this.world = new World(snakes, this);
		this.playground = new Playground(this, PLAYGROUND_SIZE_X, PLAYGROUND_SIZE_Y);

		playground.addEntity(createSnakeEntity(snakeOne, "white"));
		playground.addEntity(createSnakeEntity(snakeTwo, "red"));

		KeyBindings keyBindings = new KeyBindings(playground, snakeOne, snakeTwo);
		configureFrame();
	}

	private void resetGame()
	{
		frame.dispose();
		initGame();

	}

	/**
	 * 
	 * @param snake The victorious Snake
	 */

	public void endGame(Snake snake)
	{
		Playground.running.set(false);
		gameIsActive = false;
		boolean gameEndedInADraw = false;
		boolean gameEndedWithVictoryOfPlayerOne = false;

		if (snake == null)
		{
			gameEndedInADraw = true;
		}
		else if (snake == playerOne.getSnake())
		{
			gameEndedWithVictoryOfPlayerOne = true;
		}

		stopSnakesAndPlayers();
		String text = "";

		if (gameEndedInADraw)
		{
			text = TEXT_BOTH_SNAKES_DED;
		}
		else if (gameEndedWithVictoryOfPlayerOne)
		{
			text = TEXT_SNAKE_ONE_WAS_VICTORIOUS;
		}
		else
		{
			text = TEXT_SNAKE_TWO_WAS_VICTORIOUS;
		}

		JOptionPane.showInternalMessageDialog(playground, text);
	}

	private void stopSnakesAndPlayers()
	{
		playerOne.getSnake().setAlive(false);
		playerOne.setAlive(false);
		playerTwo.getSnake().setAlive(false);
		playerTwo.setAlive(false);
	}

	public SnakeEntity createSnakeEntity(Snake snakeOne, String color)
	{
		ArrayList<BufferedImage> imagesSnakeOne = new ArrayList<BufferedImage>();
		ArrayList<Long> timingsSnakeOne = new ArrayList<Long>();
		imagesSnakeOne.add(createColouredImage(color, PAINT_SIZE, PAINT_SIZE, false));
		timingsSnakeOne.add(500l);

		return new SnakeEntity(imagesSnakeOne, timingsSnakeOne, snakeOne);
	}

	public Entity createEntity(LinkedList<SnakePiece> pieces, String color)
	{
		ArrayList<BufferedImage> imagesSnakeOne = new ArrayList<BufferedImage>();
		ArrayList<Long> timingsSnakeOne = new ArrayList<Long>();
		imagesSnakeOne.add(createColouredImage(color, PAINT_SIZE, PAINT_SIZE, false));
		timingsSnakeOne.add(500l);

		return new Entity(imagesSnakeOne, timingsSnakeOne, pieces);
	}

	private void startGame()
	{
		Thread graphicLoop = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				playground.gameLoop();
			}

		});
		graphicLoop.start();

		Thread gameLoop = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				gameIsActive = true;
				gameLoop();
			}

		});
		gameLoop.start();
	}

	private void gameLoop()
	{
		while (gameIsActive)
		{
			Thread.yield();
			try
			{
				Thread.sleep(GAME_SPEED);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			moveSnakes();
		}
	}

	public void moveSnakes()
	{
		playerOne.getSnake().move();
		playerTwo.getSnake().move();
		world.checkForCollisions();
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
				startGame();
				startSnakes();
			}
		});
		startButton.setVisible(true);

		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				playground.running.set(true);
				resetGame();
			}
		});
		resetButton.setVisible(true);

		JPanel buttonPanel = new JPanel(new FlowLayout(1));

		buttonPanel.add(startButton);
		buttonPanel.add(resetButton);

		Dimension size = playground.getSize();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(size.height, size.width);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.add(playground, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setLocationRelativeTo(null);
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
		ArrayList<Entity> looseSnakePieces = new ArrayList<Entity>();

		Snake snake = null;

		for (int i = 0; i < AMOUNT_OF_LOOSE_SNAKEPIECES; i++)
		{
			snake = new Snake(1, 6, 6);
			world.addLooseSnakePiece(snake.getHead());
			Entity entity = createSnakeEntity(snake, "yellow");
			looseSnakePieces.add(entity);
		}
		playground.setLooseSnakePieces(looseSnakePieces);

	}

	private void createNewLooseSnakePiece()
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

		SnakePiece snakePiece = new SnakePiece(randomX, randomY, SnakePieceType.LOOSE);

		world.addLooseSnakePiece(snakePiece);
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

	public void snakeHasConsumedALoosePiece(Snake snake)
	{

		if (playerOne.getSnake() == snake)
		{
			playerOne.increasePoints(POINTS_FOR_CONSUMPTION);
		}
		else
		{
			playerTwo.increasePoints(POINTS_FOR_CONSUMPTION);
		}

		createNewLooseSnakePiece();
		playground.updateLooseSnakePieceEntities();
	}

	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	public LinkedList<SnakePiece> getLooseSnakePieces()
	{
		return world.getLooseSnakePieces();

	}
}
