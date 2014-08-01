package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import timoschwarz.snake.dao.HighscoreDAO;
import timoschwarz.snake.dao.HighscoreFileDAO;
import timoschwarz.snake.dao.Score;
import timoschwarz.snake.model.Piece;
import timoschwarz.snake.model.Player;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.World;
import timoschwarz.snake.model.powerups.Boost;
import timoschwarz.snake.model.powerups.WorldChanger;
import timoschwarz.snake.model.rules.RuleSet;
import timoschwarz.snake.util.KeyBindings;
import timoschwarz.snake.util.VideoUtils;
import timoschwarz.snake.util.WorldChangerEventTask;
import timoschwarz.snake.view.Clock;
import timoschwarz.snake.view.GameFrame;
import timoschwarz.snake.view.SnakePanel;

public class GameController
{
	private static final int DURATION_SOUND_WORLDCHANGER = 20000;
	public static final int DEFAULT_GAME_SPEED = 100;
	public static int SNAKE_SIZE = 15;
	public static int paintSize = 15;
	private static double MAX_PERCENTAGE_OF_SCREEN_SIZE = 0.6;
	private static int AMOUNT_OF_LOOSE_SNAKEPIECES = 1;
	public static int GAME_SPEED = 100;
	private static int WORLD_SIZE_X = 50;
	private static int WORLD_SIZE_Y = 50;

	public static String TEXT_GAME_OVER = "GAME OVER";
	public static String TEXT_ONE_SNAKE_WAS_VICTORIOUS = " has won!";
	public static String TEXT_BOTH_SNAKES_DEAD = "BOFS SNAIGS DED!!\nI CRI EVRYTIEM";
	public static int DURATION_SPEEDBOOSTER = 5000;
	public static int DURATION_PHASEBOOSTER = 7700;
	public static final int WORLD_GAME_SPEED_INCREASE_DURATION = 15500;
	public static final int MAX_LIGHTNING_GENERATIONS = 5;
	public static final int REPAINTS_TILL_NEXT_GENERATIONS_OF_LIGHTNINGS = 20;
	private static final int NEW_LIGHTNING_SPAWN_INTERVAL = 1000;
	public static final double WORLD_SHRINKER_MULTIPLIER = 0.95;
	public static final int WORLD_SHRINKER_ITERATIONS = 5;
	public static final int WORLD_SHRINKER_INTERVAL = 2000;
	private static final String TEXT_POSTHIGHSCORE = "Do u want to upload your Score?";
	private static final String TITLE_POSTHIGHSCORE = "Post Highscore?";
	public static int MAX_AMOUNT_OF_BOOSTER = 2;
	public static int BOOST_SPAWN_INTERVAL = 10000;
	public static int WORLDCHANGER_SPAWN_INTERVAL = 4000;
	public static int SNAKE_GROW_SIZE = 1;

	private SnakePanel snakePanel;
	private GameFrame frame;

	private AudioController audioController;
	private GraphicsController graphicsController;

	private Player playerOne;
	private Player playerTwo;

	private Timer worldChangerTimer;
	private Timer boostTimer;
	private Timer lightningTimer;

	private World world;

	private boolean gameIsActive = false;
	private JLabel scorePlayerOne;
	private JLabel scorePlayerTwo;
	private boolean worldChangerEventIsRunning = false;
	public static int REAL_FPS = 0;
	private KeyBindings keyBindings;
	private Clock clock;
	private JLabel lifesPlayerOne;
	private JLabel lifesPlayerTwo;

	private HighscoreDAO highscoreDAO;

	private RuleSet gameRules;

	public GameController(String namePlayerOne, String namePlayerTwo, RuleSet gameRules)
	{
		this.gameRules = gameRules;
		this.highscoreDAO = new HighscoreFileDAO();
		initGame(namePlayerOne, namePlayerTwo);
	}

	protected void prepareStartOfGame(String namePlayerOne, String namePlayerTwo, int playerLifes)
	{
		if (frame != null)
		{
			frame.dispose();
		}

		this.frame = new GameFrame("COMBAT SNAKEZ!!!111", this);
		this.setPlayerOne(new Player(namePlayerOne, playerLifes));
		this.setPlayerTwo(new Player(namePlayerTwo, playerLifes));
		this.graphicsController = new GraphicsController();

		Snake snakeOne = new Snake(SNAKE_SIZE, SNAKE_SIZE, 0);
		Snake snakeTwo = new Snake(SNAKE_SIZE, SNAKE_SIZE, WORLD_SIZE_Y);

		playerOne.setSnake(snakeOne);
		playerTwo.setSnake(snakeTwo);

		LinkedList<Snake> snakes = new LinkedList<Snake>();
		snakes.add(snakeOne);
		snakes.add(snakeTwo);

		this.world = new World(snakes, this, WORLD_SIZE_X, WORLD_SIZE_Y);

		calculatePaintSize();
		this.snakePanel = new SnakePanel(this, WORLD_SIZE_X, WORLD_SIZE_Y, paintSize);

		snakePanel.setSnakes(snakes);
		snakePanel.setLayout(new BorderLayout());
		keyBindings = new KeyBindings(snakePanel);
		configureFrame();
		snakePanel.updateSize();
	}

	private void configureFrame()
	{

		JPanel scorePanel = createScorePanel();
		JButton startButton = createStartButton();
		JButton resetButton = createResetButton();
		JPanel buttonPanel = createButtonPanel(startButton, resetButton);
		frame.setLayout(new BorderLayout());
		frame.setBackground(Color.BLACK);
		frame.setResizable(true);
		frame.add(snakePanel, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(scorePanel, BorderLayout.NORTH);
		frame.pack();
		Point centerPoint = calculateCenterPointForFrame();
		frame.setLocation(centerPoint);
		frame.setVisible(true);

		clock = new Clock(30);

		frame.setClock(clock);
		updatePlayerScoreLabel();
	}

	private Point calculateCenterPointForFrame()
	{
		Point centerPoint = VideoUtils.getCenterPoint();
		int x = (int) centerPoint.getX();
		int y = (int) centerPoint.getY();
		x -= frame.getPreferredSize().getWidth() / 2;
		y -= frame.getPreferredSize().getHeight() / 2;
		centerPoint.setLocation(x, y);
		return centerPoint;
	}

	private void initGame(String namePlayerOne, String namePlayerTwo)
	{
		audioController = new AudioController();
		prepareStartOfGame(namePlayerOne, namePlayerTwo, gameRules.getPlayerLifes());
	}

	private void calculatePaintSize()
	{

		int width = VideoUtils.getScreenWidth();
		width = (int) (width * MAX_PERCENTAGE_OF_SCREEN_SIZE / world.getWidth());
		int height = VideoUtils.getScreenHeight();
		height = (int) (height * MAX_PERCENTAGE_OF_SCREEN_SIZE / world.getHeight());

		paintSize = Math.min(width, height);
	}

	private void resetGame()
	{
		initGame(playerOne.getName(), playerTwo.getName());
	}

	/**
	 * 
	 * @param snake The victorious Snake
	 */

	public void processFailureOfSnake(Snake snake)
	{
		gameRules.processFailureOfSnake(getPlayerForSnake(snake), this);
	}

	public void punishSnakeForHittingTheBounds(Snake snake)
	{
		audioController.playSound("snakeHitsBounds");
		gameRules.processSnakeHittingBounds(snake, this);
	}

	private void stopTimer()
	{
		boostTimer.stop();
		worldChangerTimer.stop();
		lightningTimer.stop();

	}

	public void endGame()
	{
		audioController.playSound("comment_terminated");
		stopTimer();
		gameIsActive = false;
		String text = "";
		audioController.stopBackgroundMusic();
		Player playerWithHighestScore = getPlayerWithHighestScore();

		if (playerWithHighestScore != null)
		{
			text = text + "\n" + playerWithHighestScore.getName() + " won with a Score: "
				+ playerWithHighestScore.getScore();
		}
		else
		{
			audioController.playSound("comment_annoying");
			text = text + "\nNO ONE WON!\nNO ONE LOST! BORING...";
		}

		JOptionPane.showMessageDialog(frame, text, TEXT_GAME_OVER, JOptionPane.INFORMATION_MESSAGE);
		int postHighscore = JOptionPane.showConfirmDialog(frame, TEXT_POSTHIGHSCORE, TITLE_POSTHIGHSCORE,
			JOptionPane.OK_CANCEL_OPTION);

		if (postHighscore == 1)
		{
			highscoreDAO.insertScore(new Score(playerWithHighestScore.getName(), playerWithHighestScore.getScore()));
		}

		SnakePanel.running.set(false);
		resetGame();
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
				clock.start();
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
				processInput();
				moveSnakes();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void processInput()
	{
		playerOne.getSnake().setDirection(keyBindings.getDirectionForPlayerOne());
		playerTwo.getSnake().setDirection(keyBindings.getDirectionForPlayerTwo());
	}

	public void moveSnakes()
	{
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

		ImageIcon icon = new ImageIcon(graphicsController.getImageAsURL("pixelHeart"));
		icon.setImage(icon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

		lifesPlayerOne = new JLabel(icon);
		lifesPlayerTwo = new JLabel(icon);
		scorePlayerOne = new JLabel(textPlayerOne);
		scorePlayerTwo = new JLabel(textPlayerTwo);
		lifesPlayerOne.setText(":" + playerOne.getLifesLeft());
		lifesPlayerTwo.setText(":" + playerTwo.getLifesLeft());

		JPanel scorePanel = new JPanel();
		JPanel scorePanelPlayerOne = new JPanel();
		JPanel scorePanelPlayerTwo = new JPanel();

		Font font = getFontForScores();
		lifesPlayerOne.setFont(font);
		lifesPlayerTwo.setFont(font);
		scorePlayerOne.setFont(font);
		scorePlayerTwo.setFont(font);

		scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 0));

		scorePanelPlayerOne.setLayout(new FlowLayout(FlowLayout.LEADING));
		scorePanelPlayerTwo.setLayout(new FlowLayout(FlowLayout.TRAILING));

		scorePanelPlayerOne.add(lifesPlayerOne);
		scorePanelPlayerOne.add(scorePlayerOne);

		scorePanelPlayerTwo.add(lifesPlayerTwo);
		scorePanelPlayerTwo.add(scorePlayerTwo);

		scorePanel.add(scorePanelPlayerOne);
		scorePanel.add(scorePanelPlayerTwo);

		return scorePanel;
	}

	private String getScoreTextForPlayer(Player player)
	{
		String scoreText = player.getName() + " Score: " + player.getScore();
		return scoreText;
	}

	public void checkSnakes()
	{
		playerOne.getSnake();
	}

	public void setPlaygroundSize(Dimension size)
	{
		snakePanel.setSize(size);
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
		initLightningTimer();
	}

	private void initLightningTimer()
	{
		lightningTimer = new Timer(NEW_LIGHTNING_SPAWN_INTERVAL, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				snakePanel.addRandomLightning();
			}

		});
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
		if (!isWorldChangerEventIsRunning() && world.getAmountOfCurrentWorldChanger() == 0)
		{
			setWorldChangerEventIsRunning(true);
			audioController.playSound("worldChanger_spawn");
			java.util.Timer timer = new java.util.Timer();
			Random random = new Random();
			int delay = random.nextInt(DURATION_SOUND_WORLDCHANGER + 1) + 5;
			timer.schedule(new WorldChangerEventTask(this, timer), delay);
			spawnRandomLightnings();
		}
	}

	private void spawnRandomLightnings()
	{
		lightningTimer.start();
	}

	private void spawnLightningWhichStrikesIntoWorldChanger()
	{
		List<WorldChanger> worldChangers = world.getWorldChangers();
		Piece worldChanger = (Piece) worldChangers.get(worldChangers.size() - 1);
		int x = worldChanger.getX();
		int y = worldChanger.getY();

		snakePanel.addLightning(x, y);
	}

	public void spawnNewWorldChanger()
	{
		world.spawnNewWorldChanger();
		spawnLightningWhichStrikesIntoWorldChanger();

		updateWorldChangerOfCanvas();
		setWorldChangerEventIsRunning(false);
		lightningTimer.stop();
		snakePanel.clearLightnings();
	}

	private void updateWorldChangerOfCanvas()
	{
		snakePanel.updateWorldChangerEntities();

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
			snakePanel.clearBooster();
			return;
		}
		snakePanel.updateBooster(booster);
	}

	private void initLooseSnakePieces()
	{

		for (int i = 0; i < AMOUNT_OF_LOOSE_SNAKEPIECES; i++)
		{
			world.createNewLooseSnakePiece();
		}
		snakePanel.setLooseSnakePieces(world.getLooseSnakePieces());

	}

	public void snakeHasConsumedABooster(Snake snake, Boost booster)
	{
		audioController.playSound(booster.getSoundFileName());
		gameRules.snakeHasConsumedABooster(snake, this);

		updatePlayerScoreLabel();
		updatePlaygroundBooster();
	}

	public Player getPlayerForSnake(Snake snake)
	{
		if (snake == null)
		{
			return null;
		}

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
		gameRules.snakeHasConsumedALoosePiece(snake, this);

		updatePlayerScoreLabel();
		updateLooseSnakePiecesInView();
	}

	public void updatePlayerScoreLabel()
	{
		scorePlayerTwo.setText(getScoreTextForPlayer(playerTwo));
		scorePlayerOne.setText(getScoreTextForPlayer(playerOne));
		lifesPlayerOne.setText(": " + playerOne.getLifesLeft());
		lifesPlayerTwo.setText(": " + playerTwo.getLifesLeft());
	}

	public Font getFontForScores()
	{
		return new Font("ScoreFont", Font.PLAIN, 15);

	}

	public void updateLooseSnakePiecesInView()
	{
		snakePanel.updateLooseSnakePieceEntities();
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
		audioController.playSound(usedWorldChanger.getSoundFile());

		gameRules.snakeHasConsumedAWorldChanger(snake, this);

		updatePlayerScoreLabel();
	}

	public List<WorldChanger> getCurrentWorldChangers()
	{
		return world.getWorldChangers();
	}

	public boolean isWorldChangerEventIsRunning()
	{
		return worldChangerEventIsRunning;
	}

	public void setWorldChangerEventIsRunning(boolean worldChangerEventIsRunning)
	{
		this.worldChangerEventIsRunning = worldChangerEventIsRunning;
	}

	public void punishSnakeForHittingSnake(Snake snake)
	{
		audioController.playSound("snakeHitsSnake");
		gameRules.processSnakeHittingSnake(snake, this);
	}

	public void worldSizeHasBeenUpdated()
	{
		//calculatePaintSize();
		snakePanel.updateWorldHeight(world.getHeight());
		snakePanel.updateWorldWidth(world.getWidth());
		snakePanel.updateSize();
	}

	public RuleSet getGameRules()
	{
		return gameRules;
	}

	public List<Score> getHighscore()
	{
		return highscoreDAO.getHighscore();

	}
}
