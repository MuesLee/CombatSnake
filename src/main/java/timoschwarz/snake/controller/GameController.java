package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
import timoschwarz.snake.model.WorldChanger;
import timoschwarz.snake.util.KeyBindings;
import timoschwarz.snake.util.VideoUtils;
import timoschwarz.snake.util.WorldChangerTask;
import timoschwarz.snake.view.SnakePanel;

public class GameController
{
	public static final int POINTS_FOR_WORLDCHANGER_CONSUMPTION = 150;
	private static final int DURATION_SOUND_WORLDCHANGER = 20000;
	public static int SNAKE_SIZE = 15;
	public static int paintSize = 15;
	private static double MAX_PERCENTAGE_OF_SCREEN_SIZE = 0.7;
	private static int AMOUNT_OF_LOOSE_SNAKEPIECES = 1;
	private static int POINTS_FOR_FOOD_CONSUMPTION = 10;
	private static int POINTS_FOR_BOOSTER_CONSUMPTION = 50;
	private static int GAME_SPEED = 100;
	private static int WORLD_SIZE_X = 50;
	private static int WORLD_SIZE_Y = 50;

	public static String TEXT_GAME_OVER = "GAME OVER";
	public static String TEXT_ONE_SNAKE_WAS_VICTORIOUS = " has won!";
	public static String TEXT_BOTH_SNAKES_DEAD = "BOFS SNAIGS DED!!\nI CRI EVRYTIEM";
	public static int DURATION_SPEEDBOOSTER = 5000;
	public static int DURATION_PHASEBOOSTER = 7700;
	public static int MAX_AMOUNT_OF_BOOSTER = 2;
	public static int BOOST_SPAWN_INTERVAL = 10000;
	public static int WORLDCHANGER_SPAWN_INTERVAL = 6000;
	public static int SNAKE_GROW_SIZE = 1;

	private SnakePanel playground;
	private JFrame frame;

	private AudioController audioController;

	private Player playerOne;
	private Player playerTwo;

	private Timer worldChangerTimer;
	private Timer boostTimer;

	private World world;

	private boolean gameIsActive = false;
	private JLabel scorePlayerOne;
	private JLabel scorePlayerTwo;
	private boolean worldChangerEventIsRunning = false;
	public static int PENALTY_FOR_FAILURE = 4;

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
		@SuppressWarnings("unused")
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

		stopTimer();

		audioController.stopBackgroundMusic();

		boolean gameEndedInADraw = false;
		boolean gameEndedWithFailureOfPlayerTwo = false;

		if (snake == null)
		{
			gameEndedInADraw = true;
		}
		else if (snake == playerOne.getSnake())
		{
			gameEndedWithFailureOfPlayerTwo = true;
		}

		String text = "";

		Random random = new Random();

		if (gameEndedInADraw)
		{
			audioController.playSound("comment_annoying");
			text = TEXT_BOTH_SNAKES_DEAD;
		}
		else if (gameEndedWithFailureOfPlayerTwo)
		{
			audioController.playSound("comment_terminated");
			int score = playerTwo.getScore() / random.nextInt(GameController.PENALTY_FOR_FAILURE);
			playerTwo.setScore(score);
		}
		else
		{
			//PLAYER ONE FAILED
			audioController.playSound("comment_terminated");
			int score = playerOne.getScore() / (random.nextInt(GameController.PENALTY_FOR_FAILURE) + 1);
			playerOne.setScore(score);
		}
		Player playerWithHighestScore = getPlayerWithHighestScore();

		if (playerWithHighestScore != null)
		{
			text = text + "\n" + playerWithHighestScore.getName() + " won with a Score: "
				+ playerWithHighestScore.getScore();
		}
		else
		{
			text = text + "\nNO ONE WON!\nNO ONE LOST! BORING...";
		}

		JOptionPane.showMessageDialog(frame, text, TEXT_GAME_OVER, JOptionPane.INFORMATION_MESSAGE);
		SnakePanel.running.set(false);
	}

	private void stopTimer()
	{
		boostTimer.stop();
		worldChangerTimer.stop();

	}

	private Player getPlayerWithHighestScore()
	{
		final int scorePlayerOne = playerOne.getScore();
		final int scorePlayerTwo = playerTwo.getScore();
		if (scorePlayerOne > scorePlayerTwo)
		{
			return playerOne;
		}
		else if (scorePlayerOne < scorePlayerTwo)
		{
			return playerTwo;
		}
		return null;

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

		SnakePanel.running.set(true);
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
				SnakePanel.running.set(true);
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
		initTimer();
	}

	private void initTimer()
	{
		initBoostTimer();
		initWorldChangerTimer();
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

	private void initWorldChangerTimer()
	{

		this.worldChangerTimer = new Timer(WORLDCHANGER_SPAWN_INTERVAL, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				triggerWorldChangerSpawn();
			}

		});

		worldChangerTimer.start();
	}

	private void triggerWorldChangerSpawn()
	{
		if (!worldChangerEventIsRunning)
		{
			worldChangerEventIsRunning = true;
			audioController.playSound("worldChanger_spawn");
			java.util.Timer timer = new java.util.Timer();
			Random random = new Random();
			int delay = random.nextInt(DURATION_SOUND_WORLDCHANGER + 1);
			timer.schedule(new WorldChangerTask(this, timer), delay);
		}
	}

	public void spawnNewWorldChanger()
	{
		world.spawnNewWorldChanger();
		updateWorldChangerOfCanvas();
		worldChangerEventIsRunning = false;
	}

	private void updateWorldChangerOfCanvas()
	{
		playground.getCanvas().updateWorldChangerEntities();

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

	public void snakeHasConsumedABooster(Snake snake, Boost booster)
	{
		Player player = getPlayerForSnake(snake);
		player.increaseScore(POINTS_FOR_BOOSTER_CONSUMPTION);
		updatePlayerScoreLabel();
		audioController.playSound(booster.getSoundFileName());
		updatePlaygroundBooster();
	}

	private Player getPlayerForSnake(Snake snake)
	{
		if (playerOne.getSnake() == snake)
		{
			return playerOne;
		}
		else
		{
			return playerTwo;
		}
	}

	public void snakeHasConsumedALoosePiece(Snake snake)
	{
		audioController.playSound("bite");
		if (playerOne.getSnake() == snake)
		{
			playerOne.increaseScore(calculatePointsForGrowing(snake));
		}
		else
		{
			playerTwo.increaseScore(calculatePointsForGrowing(playerTwo.getSnake()));
		}

		updatePlayerScoreLabel();

		world.createNewLooseSnakePiece();
		updateLooseSnakePiecesOfCanvas();
	}

	private void updatePlayerScoreLabel()
	{
		scorePlayerTwo.setText(getScoreTextForPlayer(playerTwo));
		scorePlayerOne.setText(getScoreTextForPlayer(playerOne));
	}

	public void updateLooseSnakePiecesOfCanvas()
	{
		playground.getCanvas().updateLooseSnakePieceEntities();
	}

	private int calculatePointsForGrowing(Snake snake)
	{
		return POINTS_FOR_FOOD_CONSUMPTION * (snake.getGrowSize() + 1);
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

	public void snakeHasConsumedAWorldChanger(Snake snake, WorldChanger usedWorldChanger)
	{
		Player player = getPlayerForSnake(snake);
		player.increaseScore(POINTS_FOR_WORLDCHANGER_CONSUMPTION);
		updatePlayerScoreLabel();
		audioController.playSound(usedWorldChanger.getSoundFile());
	}

	public List<WorldChanger> getCurrentWorldChangers()
	{
		return world.getWorldChangers();
	}

}
