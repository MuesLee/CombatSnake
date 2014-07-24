package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import timoschwarz.snake.model.Boost;
import timoschwarz.snake.model.Piece;
import timoschwarz.snake.model.Player;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.World;
import timoschwarz.snake.util.KeyBindings;
import timoschwarz.snake.util.VideoUtils;
import timoschwarz.snake.view.SnakePanel;

public class GameController
{
	public static final int SNAKE_SIZE = 15;
	public static int paintSize = 15;
	private static double MAX_PERCENTAGE_OF_SCREEN_SIZE = 0.7;
	private static final int AMOUNT_OF_LOOSE_SNAKEPIECES = 1;
	private static final int POINTS_FOR_CONSUMPTION = 10;
	private static final int GAME_SPEED = 100;
	private static final int WORLD_SIZE_X = 50;
	private static final int WORLD_SIZE_Y = 50;

	public static final String TEXT_GAME_OVER = "GAME OVER";
	public static final String TEXT_ONE_SNAKE_WAS_VICTORIOUS = " has won!";
	public static final String TEXT_BOTH_SNAKES_DEAD = "BOFS SNAIGS DED!!\nI CRI EVRYTIEM";
	public static final int DURATION_SPEEDBOOSTER = 5000;
	public static final int DURATION_PHASEBOOSTER = 7700;
	public static final int MAX_AMOUNT_OF_BOOSTER = 2;
	public static final int BOOST_SPAWN_INTERVAL = 10000;
	public static int SNAKE_GROW_SIZE = 1;

	private SnakePanel playground;
	private JFrame frame;

	private AudioController audioController;

	private Player playerOne;
	private Player playerTwo;

	private Timer boostTimer;

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
		Snake snakeTwo = new Snake(SNAKE_SIZE, SNAKE_SIZE, WORLD_SIZE_Y);

		playerOne.setSnake(snakeOne);
		playerTwo.setSnake(snakeTwo);

		LinkedList<Snake> snakes = new LinkedList<Snake>();
		snakes.add(snakeOne);
		snakes.add(snakeTwo);

		this.world = new World(snakes, this, WORLD_SIZE_X, WORLD_SIZE_Y);
		this.playground = new SnakePanel(this, WORLD_SIZE_X * paintSize, WORLD_SIZE_Y * paintSize, paintSize);
		playground.getCanvas().setSnakes(snakes);
		KeyBindings keyBindings = new KeyBindings(playground, snakeOne, snakeTwo);
		configureFrame();
	}

	private void initGame(String namePlayerOne, String namePlayerTwo)
	{
		audioController = new AudioController();
		if (boostTimer != null)
		{
			boostTimer.stop();
		}
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
		boostTimer.stop();

		audioController.stopBackgroundMusic();

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
			audioController.playSound("comment_annoying");
			text = TEXT_BOTH_SNAKES_DEAD;
		}
		else if (gameEndedWithVictoryOfPlayerOne)
		{
			audioController.playSound("comment_terminated");
			text = playerOne.getName() + TEXT_ONE_SNAKE_WAS_VICTORIOUS + "\nScore: " + playerOne.getScore();
		}
		else
		{
			audioController.playSound("comment_terminated");
			text = playerTwo.getName() + TEXT_ONE_SNAKE_WAS_VICTORIOUS + "\nScore: " + playerTwo.getScore();
		}

		JOptionPane.showMessageDialog(frame, text, TEXT_GAME_OVER, JOptionPane.INFORMATION_MESSAGE);
		SnakePanel.running.set(false);
	}

	void startGame()
	{
		audioController.startBackgroundMusic();

		Thread graphicLoop = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				playground.gameLoop();
			}

		});

		playground.running.set(true);
		graphicLoop.start();

		gameIsActive = true;
		Thread gameLoop = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
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
		//		System.out.println("P1: " + playerOne.getSnake().getHead());
		//		System.out.println("P2: " + playerTwo.getSnake().getHead());

		final Snake snakeOne = playerOne.getSnake();
		int movementSpeedOne = snakeOne.getMovementSpeed();

		final Snake snakeTwo = playerTwo.getSnake();
		int movementSpeedTwo = snakeTwo.getMovementSpeed();

		int maxMovementSpeed = Math.max(movementSpeedOne, movementSpeedTwo);

		for (int i = 0; i < maxMovementSpeed; i++)
		{
			snakeOne.move(i);
			snakeTwo.move(i);
			world.checkForCollisions();
		}
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
				SnakePanel.running.set(true);
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
		initBoostTimer();
	}

	private void initBoostTimer()
	{

		this.boostTimer = new Timer(BOOST_SPAWN_INTERVAL, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				triggerBoostSpawn();
			}
		});

		boostTimer.start();
	}

	private void triggerBoostSpawn()
	{
		if (MAX_AMOUNT_OF_BOOSTER > world.getAmountOfCurrentBooster())
		{
			audioController.playSound("boost_spawn");
			world.spawnNewBooster();
			updatePlaygroundBooster();
		}
	}

	private void updatePlaygroundBooster()
	{
		List<Boost> booster = world.getCurrentBooster();

		if (booster.isEmpty())
		{
			playground.getCanvas().clearBooster();
			return;
		}
		playground.getCanvas().updateBooster(booster);
	}

	private void initLooseSnakePieces()
	{

		for (int i = 0; i < AMOUNT_OF_LOOSE_SNAKEPIECES; i++)
		{
			world.createNewLooseSnakePiece();
		}
		playground.getCanvas().setLooseSnakePieces(world.getLooseSnakePieces());

	}

	public void snakeHasConsumedABooster(Boost booster)
	{
		audioController.playSound(booster.getSoundFileName());
		updatePlaygroundBooster();
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
		playground.getCanvas().updateLooseSnakePieceEntities();
	}

	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	public LinkedList<Piece> getLooseSnakePieces()
	{
		return world.getLooseSnakePieces();

	}

	public List<Boost> getCurrentBooster()
	{
		return world.getCurrentBooster();

	}

}
