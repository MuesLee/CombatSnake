package timoschwarz.snake.model;

public class Player
{
	private String name;
	private int points;
	private Snake snake;

	public Player(String name)
	{
		this.name = name;
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

	public void setPoints(int points)
	{
		this.points = points;
	}

	public void increasePoints(int points)
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

}
