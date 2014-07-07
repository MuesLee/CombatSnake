package timoschwarz.snake.model;

import timoschwarz.util.KeyboardSettings;


public class Player
{
	private String name;
	private int points;
	private Snake snake;

	public Player(String name, KeyboardSettings keyboardSettings)
	{
		this.name = name;
		this.keyboardSettings = keyboardSettings;
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

	private KeyboardSettings keyboardSettings;

	public Snake getSnake()
	{
		return snake;
	}

	public void setSnake(Snake snake)
	{
		this.snake = snake;
	}

}
