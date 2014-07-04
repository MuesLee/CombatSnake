package timoschwarz.snake;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Controller implements KeyListener
{
	private Playground playground;
	private JFrame frame;

	private Player playerOne;
	private Player playerTwo;

	public Controller()
	{
		this.setPlayerOne(new Player("Player One", null));
		this.frame = new JFrame("COMBAT SNAKEZ!!!111");

		Snake snakeOne = new Snake(4);
		playerOne.setSnake(snakeOne);
		ArrayList<Snake> snakes = new ArrayList<Snake>(2);
		snakes.add(snakeOne);

		this.playground = new Playground(new Dimension(500, 500), snakes);
	}

	public void showPlayground()
	{
		configureFrame();
		playground.setVisible(true);
		frame.pack();
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

	private void checkSnakeMovement(Snake snake, Direction direction)
	{
		SnakePiece headWithNextPosition = snake.createHeadWithNextPosition(direction);

		if (!pieceIsOutOfBounds(headWithNextPosition))
		{
			snake.move(direction);
		}
		else if (headIsHittingALooseSnakePiece(headWithNextPosition))
		{
			startConsumeProcess();
		}
		else if (pieceCollidesWithOtherSnakePiece(headWithNextPosition))
		{
			endGame();
		}
		else
		{
			endGame();
		}
	}

	private void startConsumeProcess()
	{
		//TODO: Punkte erhöhen
	}

	private boolean pieceCollidesWithOtherSnakePiece(SnakePiece headWithNextPosition)
	{
		return false;
	}

	private boolean headIsHittingALooseSnakePiece(SnakePiece headWithNextPosition)
	{
		return false;
	}

	private boolean pieceIsOutOfBounds(SnakePiece headWithNextPosition)
	{
		int x = headWithNextPosition.getX();
		int y = headWithNextPosition.getY();
		Dimension size = playground.getSize();

		return x < 0 || y < 0 || x > size.width || y > size.height;
	}

	private void endGame()
	{
		System.out.println("Shit's over!");
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
		checkSnakeMovement(snake, direction);
		playground.repaint();
	}

	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}
}
