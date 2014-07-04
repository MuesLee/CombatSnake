package timoschwarz.snake;

import java.awt.Dimension;

public class Playground
{
	private Dimension size;

	public Playground(Dimension size)
	{
		this.setSize(size);
	}

	public Dimension getSize()
	{
		return size;
	}

	public void setSize(Dimension size)
	{
		this.size = size;
	}

}
