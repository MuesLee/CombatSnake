package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import timoschwarz.snake.model.Player;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;
import timoschwarz.snake.model.World;
import timoschwarz.snake.view.Entity;
import timoschwarz.snake.view.Playground;
import timoschwarz.util.EntityHelper;
import timoschwarz.util.KeyBindings;
import timoschwarz.util.VideoUtils;

public class GameController
{
	public static final int SNAKE_SIZE = 15;
	public static int paintSize = 15;
	private static double MAX_PERCENTAGE_OF_SCREEN_SIZE = 0.7;
	private static final int AMOUNT_OF_LOOSE_SNAKEPIECES = 1;
	private static final int POINTS_FOR_CONSUMPTION = 10;
	private static final int GAME_SPEED = 80;
	private static final int WORLD_SIZE_X = 50;
	private static final int WORLD_SIZE_Y = 50;

	public static final String TEXT_GAME_OVER = "GAME OVER";
	public static final String TEXT_SNAKE_ONE_WAS_VICTORIOUS = "Snake One has won!";
	public static final String TEXT_SNAKE_TWO_WAS_VICTORIOUS = "Snake Two has won!";
	public static final String TEXT_BOTH_SNAKES_DEAD = "BOFS SNAIGS DED!!";

	private Playground playground;
	private JFrame frame;

	private AudioController audioController;

	private Player playerOne;
	private Player playerTwo;

	private World world;

	private boolean gameIsActive = false;
	private JLabel scorePlayerOne;
	private JLabel scorePlayerTwo;

	public GameController(String namePlayerOne, String namePlayerTwo)
	{
		initGame(namePlayerOne, namePlayerTwo);

	}

	protected void prepareStartOfGame(String namePlayerOne, String namePlayerTwo)
	{
		calculatePaintSize();

		this.setPlayerOne(new Player(namePlayerOne));
		this.setPlayerTwo(new Player(namePlayerTwo));
		this.frame = new JFrame("COMBAT SNAKEZ!!!111");

		Snake snakeOne = new Snake(SNAKE_SIZE, SNAKE_SIZE, 0);
		Snake snakeTwo = new Snake(SNAKE_SIZE, SNAKE_SIZE, 10);

		playerOne.setSnake(snakeOne);
		playerTwo.setSnake(snakeTwo);

		LinkedList<Snake> snakes = new LinkedList<Snake>();
		snakes.add(snakeOne);
		snakes.add(snakeTwo);

		this.world = new World(snakes, this, WORLD_SIZE_X, WORLD_SIZE_Y);
		this.playground = new Playground(this, WORLD_SIZE_X * paintSize, WORLD_SIZE_Y * paintSize);

		playground.addEntity(EntityHelper.createSnakeEntity(snakeOne, "white"));
		playground.addEntity(EntityHelper.createSnakeEntity(snakeTwo, "red"));

		KeyBindings keyBindings = new KeyBindings(playground, snakeOne, snakeTwo);
		configureFrame();
	}

	private void initGame(String namePlayerOne, String namePlayerTwo)
	{
		audioController = new AudioController();
		audioController.startMenuBackgroundMusic();
		prepareStartOfGame(namePlayerOne, namePlayerTwo);

	}

	private void calculatePaintSize()
	{

		int width = VideoUtils.getScreenWidth();
		width = (int) (width * MAX_PERCENTAGE_OF_SCREEN_SIZE / WORLD_SIZE_X);
		int height = VideoUtils.getScreenHeight();
		height = (int) (height * MAX_PERCENTAGE_OF_SCREEN_SIZE / WORLD_SIZE_Y);

		paintSize = Math.min(width, height);

	}

	private void resetGame()
	{
		frame.dispose();
		initGame(playerOne.getName(), playerTwo.getName());

	}

	/**
	 * 
	 * @param snake The victorious Snake
	 */

	public void endGame(Snake snake)
	{
		gameIsActive = false;

		audioController.stopBackgroundMusic();
		audioController.playSound("comment_terminated");

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

		String text = "";

		if (gameEndedInADraw)
		{
			audioController.playSound("annoying");
			text = TEXT_BOTH_SNAKES_DEAD;
		}
		else if (gameEndedWithVictoryOfPlayerOne)
		{
			text = TEXT_SNAKE_ONE_WAS_VICTORIOUS;
		}
		else
		{
			text = TEXT_SNAKE_TWO_WAS_VICTORIOUS;
		}

		JOptionPane.showMessageDialog(frame, text, TEXT_GAME_OVER, JOptionPane.INFORMATION_MESSAGE);
		Playground.running.set(false);
	}

	private void startGame()
	{
		audioController.stopMenuBackgroundMusic();
		audioController.startBackgroundMusic();

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
			try
			{
				Thread.sleep(GAME_SPEED);
				moveSnakes();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
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
		JPanel scorePanel = createScorePanel();
		JButton startButton = createStartButton();
		JButton resetButton = createResetButton();
		JPanel buttonPanel = createButtonPanel(startButton, resetButton);

		Dimension size = playground.getSize();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(size.height, size.width);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.add(playground, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(scorePanel, BorderLayout.NORTH);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private JPanel createButtonPanel(JButton startButton, JButton resetButton)
	{
		JPanel buttonPanel = new JPanel(new FlowLayout(1));

		buttonPanel.add(startButton);
		buttonPanel.add(resetButton);
		return buttonPanel;
	}

	private JButton createResetButton()
	{
		JButton resetButton = new JButton("Reset");
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
		return resetButton;
	}

	private JButton createStartButton()
	{
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Playground.running.set(true);
				startGame();
				startSnakes();
			}
		});
		startButton.setVisible(true);
		return startButton;
	}

	private JPanel createScorePanel()
	{
		String textPlayerOne = getScoreTextForPlayer(playerOne);
		String textPlayerTwo = getScoreTextForPlayer(playerTwo);
		JPanel scorePanel = new JPanel();
		scorePlayerOne = new JLabel(textPlayerOne);
		scorePlayerTwo = new JLabel(textPlayerTwo);
		scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		scorePanel.add(scorePlayerOne);
		scorePanel.add(scorePlayerTwo);
		return scorePanel;
	}

	private String getScoreTextForPlayer(Player player)
	{
		String scoreText = player.getName() + ": " + player.getScore();
		return scoreText;
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

		for (int i = 0; i < AMOUNT_OF_LOOSE_SNAKEPIECES; i++)
		{
			world.createNewLooseSnakePiece();
			world.getLooseSnakePieces();

			Entity entity = EntityHelper.createEntity(world.getLooseSnakePieces(), "red");
			looseSnakePieces.add(entity);
		}
		playground.setLooseSnakePieces(looseSnakePieces);

	}

	public void snakeHasConsumedALoosePiece(Snake snake)
	{
		audioController.playSound("bite");
		if (playerOne.getSnake() == snake)
		{
			playerOne.increasePoints(POINTS_FOR_CONSUMPTION);
			scorePlayerOne.setText(getScoreTextForPlayer(playerOne));
		}
		else
		{
			playerTwo.increasePoints(POINTS_FOR_CONSUMPTION);
			scorePlayerTwo.setText(getScoreTextForPlayer(playerTwo));
		}

		world.createNewLooseSnakePiece();
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
