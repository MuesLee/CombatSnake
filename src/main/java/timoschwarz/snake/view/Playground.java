package timoschwarz.snake.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;

import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;

public class Playground extends JPanel implements Runnable
{
	private static final long serialVersionUID = -4762811280627220936L;
	private static final int PAINT_SIZE = 15;

	private Dimension size;
	private LinkedList<Snake> snakes;

	private Color[] snakeColors = new Color[2];

	public Playground(Dimension size, LinkedList<Snake> snakes)
	{
		this.snakes = snakes;
		this.setSize(size);
		this.setPreferredSize(size);
		this.setBackground(Color.BLACK);
		this.snakeColors[0] = Color.WHITE;
		this.snakeColors[1] = Color.RED;
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		for (int i = 0; i < snakes.size(); i++)
		{
			g.setColor(snakeColors[i]);
			Snake snake = snakes.get(i);
			LinkedList<SnakePiece> pieces = snake.getPieces();
			Iterator<SnakePiece> iterator = pieces.iterator();

			while (iterator.hasNext())
			{
				SnakePiece snakePiece = iterator.next();
				int x = snakePiece.getX();
				int y = snakePiece.getY();
				g.fillRect(x * PAINT_SIZE, y * PAINT_SIZE, PAINT_SIZE, PAINT_SIZE);
			}
		}
	}

	@Override
	public Dimension getSize()
	{
		return size;
	}

	@Override
	public void setSize(Dimension size)
	{
		this.size = size;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
		}
	}

}
