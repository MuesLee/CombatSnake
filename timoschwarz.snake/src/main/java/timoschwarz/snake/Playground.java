package timoschwarz.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Playground extends JPanel
{
	private static final long serialVersionUID = -4762811280627220936L;
	private static final int PAINT_SIZE = 15;

	private Dimension size;
	private ArrayList<Snake> snakes;

	public Playground(Dimension size, ArrayList<Snake> snakes)
	{
		this.snakes = snakes;
		this.setSize(size);
		this.setPreferredSize(size);
		this.setBackground(Color.BLACK);
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		g.setColor(Color.WHITE);

		for (Snake snake : snakes)
		{
			List<SnakePiece> pieces = snake.getPieces();

			for (int i = 0; i < pieces.size(); i++)
			{
				SnakePiece snakePiece = pieces.get(i);
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

}
