package timoschwarz.snake.view;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import timoschwarz.snake.controller.Controller;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;

public class Entity extends Animator
{

	public boolean visible = false;
	private LinkedList<Rectangle2D.Double> rects;
	private Snake snake;
	private Rectangle2D.Double snakeHead;

	public Entity(ArrayList<BufferedImage> images, ArrayList<Long> timings, Snake snake)
	{
		super(images, timings);
		this.setSnake(snake);
		visible = true;
		setRects(new LinkedList<Rectangle2D.Double>());
		fillRects();

	}

	private void fillRects()
	{
		int paintSize = Controller.PAINT_SIZE;
		LinkedList<SnakePiece> pieces = getSnake().getPieces();
		SnakePiece first = pieces.getFirst();
		setSnakeHead(new Rectangle2D.Double(first.getX() * paintSize + Playground.BORDER_THICKNESS, first.getY()
			* paintSize + Playground.BORDER_THICKNESS, getCurrentImage().getWidth(), getCurrentImage().getHeight()));
		first = null;

		for (SnakePiece snakePiece : pieces)
		{
			getRects().add(
				new Rectangle2D.Double(snakePiece.getX() * paintSize + Playground.BORDER_THICKNESS, snakePiece.getY()
					* paintSize + Playground.BORDER_THICKNESS, getCurrentImage().getWidth(), getCurrentImage()
					.getHeight()));
		}
		snakeHead = rects.getFirst();

	}

	private void updateRects()
	{
		LinkedList<SnakePiece> pieces = snake.getPieces();

		int paintSize = Controller.PAINT_SIZE;

		for (int i = 0; i < rects.size(); i++)
		{
			Rectangle2D.Double rect = rects.get(i);
			rect.x = pieces.get(i).getX() * paintSize + Playground.BORDER_THICKNESS;
			rect.y = pieces.get(i).getY() * paintSize + Playground.BORDER_THICKNESS;
		}

	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public HashSet<String> getMask(Entity e)
	{
		HashSet<String> mask = new HashSet<String>();
		int pixel, a;
		BufferedImage bi = e.getCurrentImage();//gets the current image being shown
		for (int i = 0; i < bi.getWidth(); i++)
		{ // for every (x,y) component in the given box, 
			for (int j = 0; j < bi.getHeight(); j++)
			{
				pixel = bi.getRGB(i, j); // get the RGB value of the pixel
				a = pixel >> 24 & 0xff;
				if (a != 0)
				{ // if the alpha is not 0, it must be something other than transparent
					mask.add(e.getX() + i + "," + (e.getY() - j)); // add the absolute x and absolute y coordinates to our set
				}
			}
		}
		return mask; //return our set
	}

	// Returns true if there is a collision between object a and object b   
	public boolean checkPerPixelCollision(Entity b)
	{
		// This method detects to see if the images overlap at all. If they do, collision is possible
		int ax1 = (int) getX();
		int ay1 = (int) getY();

		int ax2 = ax1 + (int) getWidth();
		int ay2 = ay1 + (int) getHeight();

		int bx1 = (int) b.getX();
		int by1 = (int) b.getY();

		int bx2 = bx1 + (int) b.getWidth();

		int by2 = by1 + (int) b.getHeight();

		if (by2 < ay1 || ay2 < by1 || bx2 < ax1 || ax2 < bx1)
		{
			return false; // Collision is impossible.
		}
		else
		{
			HashSet<String> maskPlayer1 = getMask(this);
			HashSet<String> maskPlayer2 = getMask(b);
			maskPlayer1.retainAll(maskPlayer2);
			if (maskPlayer1.size() > 0)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void update(long elapsedTime)
	{
		super.update(elapsedTime);
		getWidth();
		getHeight();
	}

	public boolean intersects(Entity e)
	{
		boolean intersects = false;

		LinkedList<Rectangle2D.Double> rectsToCheck = e.getRects();
		for (Rectangle2D.Double rect : rectsToCheck)
		{
			if (getSnakeHead() == rect)
			{
				continue;
			}

			if (getSnakeHead().intersects(rect))
			{
				return true;
			}
		}

		return intersects;
	}

	public double getX()
	{
		return getSnakeHead().x;
	}

	public double getY()
	{
		return getSnakeHead().y;
	}

	public double getWidth()
	{
		if (getCurrentImage() == null)
		{
			return getSnakeHead().width = 0;
		}

		return getSnakeHead().width = getCurrentImage().getWidth();
	}

	public double getHeight()
	{
		if (getCurrentImage() == null)
		{
			return getSnakeHead().height = 0;
		}
		return getSnakeHead().height = getCurrentImage().getHeight();
	}

	public LinkedList<Rectangle2D.Double> getRects()
	{
		return rects;
	}

	public void setRects(LinkedList<Rectangle2D.Double> rects)
	{
		this.rects = rects;
	}

	public void move()
	{
		updateRects();
	}

	public Snake getSnake()
	{
		return snake;
	}

	public void setSnake(Snake snake)
	{
		this.snake = snake;
	}

	public Rectangle2D.Double getSnakeHead()
	{
		return snakeHead;
	}

	public void setSnakeHead(Rectangle2D.Double snakeHead)
	{
		this.snakeHead = snakeHead;
	}

	@Override
	public String toString()
	{
		return "KOPF X: " + snakeHead.getX() + " Y: " + snakeHead.getY();
	}
}