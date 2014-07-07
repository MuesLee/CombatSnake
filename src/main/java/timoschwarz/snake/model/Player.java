package timoschwarz.snake.model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import timoschwarz.util.Direction;
import timoschwarz.util.KeyboardSettings;

public class Player implements KeyListener
{
	private String name;
	private int points;
	private Snake snake;
	private KeyboardSettings keyboardSettings;

	public Player(String name, KeyboardSettings keyboardSettings)
	{
		this.name = name;
		this.keyboardSettings = keyboardSettings;
	}

	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();

		Direction direction = KeyboardSettings.getDirectionForKey(keyCode);

		snake.setDirection(direction);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getPoints()
	{
		return points;
	}

	public void setPoints(int points)
	{
		this.points = points;
	}

	public void increasePoints(int points)
	{
		this.points += points;
	}

	public KeyboardSettings getKeyboardSettings()
	{
		return keyboardSettings;
	}

	public void setKeyboardSettings(KeyboardSettings keyboardSettings)
	{
		this.keyboardSettings = keyboardSettings;
	}

	public Snake getSnake()
	{
		return snake;
	}

	public void setSnake(Snake snake)
	{
		this.snake = snake;
	}

	public void keyReleased(KeyEvent arg0)
	{

	}

	public void keyTyped(KeyEvent arg0)
	{

	}

}
