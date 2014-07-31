package timoschwarz.snake.model;

public class Player
{
	private String name;
	private int points;
	private Snake snake;
	private int lifesLeft;

	public Player(String name, int lifes)
	{
		this.name = name;
		this.lifesLeft = lifes;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getScore()
	{
		return points;
	}

	public void setScore(int points)
	{
		this.points = points;
	}

	public void increaseScore(int points)
	{
		this.points += points;
	}

	public Snake getSnake()
	{
		return snake;
	}

	public void setSnake(Snake snake)
	{
		this.snake = snake;
	}

	@Override
	public String toString()
	{
		return name;
	}

	public int getLifesLeft()
	{
		return lifesLeft;
	}

	public void setLifesLeft(int lifesLeft)
	{
		this.lifesLeft = lifesLeft;
	}

}
