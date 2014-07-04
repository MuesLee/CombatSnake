package timoschwarz.snake;

public class Diff
{
	private int difX;
	private int difY;

	public Diff(int difX, int difY)
	{
		this.setDifX(difX);
		this.setDifY(difY);
	}

	public int getDifY()
	{
		return difY;
	}

	public void setDifY(int difY)
	{
		this.difY = difY;
	}

	public int getDifX()
	{
		return difX;
	}

	public void setDifX(int difX)
	{
		this.difX = difX;
	}

}
