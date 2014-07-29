package timoschwarz.snake.model;

public class Coordinates
{
	private int x;
	private int y;

	public Coordinates(int x, int y)
	{
		this.setX(x);
		this.y = y;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	@Override
	public String toString()
	{

		return "X: " + x + " Y: " + y;
	}

}
