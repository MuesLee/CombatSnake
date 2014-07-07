package timoschwarz.snake.model;


public class Player
{
	private String name;
	private int points;
	private Snake snake;
	private boolean isAlive = true;

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

	public Snake getSnake()
	{
		return snake;
	}

	public void setSnake(Snake snake)
	{
		this.snake = snake;
	}

	public boolean isAlive()
	{
		return isAlive;
	}

	public void setAlive(boolean isAlive)
	{
		this.isAlive = isAlive;
	}

}
