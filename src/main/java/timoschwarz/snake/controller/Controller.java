package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import timoschwarz.snake.model.Player;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.view.Playground;
import timoschwarz.util.Direction;

public class Controller implements KeyListener, Observer
{
	private Playground playground;
	private JFrame frame;

	private Referee referee;

	private Player playerOne;
	private Player playerTwo;

	public Controller()
	{

		this.setPlayerOne(new Player("Player One", null));
		this.setPlayerTwo(new Player("Player Two", null));
		this.frame = new JFrame("COMBAT SNAKEZ!!!111");

		Snake snakeOne = new Snake(15);
		Snake snakeTwo = new Snake(15);

		playerOne.setSnake(snakeOne);
		playerTwo.setSnake(snakeTwo);

		LinkedList<Snake> snakes = new LinkedList<Snake>();
		snakes.add(snakeOne);
		snakes.add(snakeTwo);

		this.playground = new Playground(new Dimension(500, 500), snakes);
		this.setReferee(new Referee(playground, snakes));
		referee.addObserver(this);
	}

	public void showPlayground()
	{
		configureFrame();
		playground.setVisible(true);
		frame.pack();
		Thread threadPaint = new Thread(playground);
		threadPaint.start();
	}

	private void configureFrame()
	{
		Dimension size = playground.getSize();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(size.height, size.width);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
		frame.addKeyListener(this);
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

	public void keyPressed(KeyEvent e)
	{
		char keyChar = e.getKeyChar();
		Direction direction = null;
		switch (keyChar)
		{
			case 'w':
				direction = Direction.UP;
			break;
			case 's':
				direction = Direction.DOWN;
			break;
			case 'a':
				direction = Direction.LEFT;
			break;
			case 'd':
				direction = Direction.RIGHT;
			break;

			default:
			break;
		}

		Snake snake = playerOne.getSnake();
		snake.setDirection(direction);
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

	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

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
}
