package timoschwarz.snake.model;

public class Piece
{

	protected int x;
	protected int y;

	public Piece(int x, int y)
	{
		this.x = x;
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

}
