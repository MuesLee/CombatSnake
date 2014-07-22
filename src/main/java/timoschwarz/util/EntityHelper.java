package timoschwarz.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;
import timoschwarz.snake.view.Entity;
import timoschwarz.snake.view.SnakeEntity;

public class EntityHelper
{
	public static SnakeEntity createSnakeEntity(Snake snake, String color)
	{
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		ArrayList<Long> timings = new ArrayList<Long>();
		images.add(createColouredImage(color, GameController.paintSize, GameController.paintSize, false));
		timings.add(500l);

		return new SnakeEntity(images, timings, snake);
	}

	public static Entity createEntity(LinkedList<SnakePiece> pieces, String color)
	{
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		ArrayList<Long> timings = new ArrayList<Long>();
		images.add(createColouredImage(color, GameController.paintSize, GameController.paintSize, false));
		timings.add(500l);

		return new Entity(images, timings, pieces);
	}

	public static BufferedImage createColouredImage(String color, int w, int h, boolean circular)
	{
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		switch (color.toLowerCase())
		{
			case "green":
				g2.setColor(Color.GREEN);
			break;
			case "magenta":
				g2.setColor(Color.MAGENTA);
			break;
			case "red":
				g2.setColor(Color.RED);
			break;
			case "yellow":
				g2.setColor(Color.YELLOW);
			break;
			case "blue":
				g2.setColor(Color.BLUE);
			break;
			case "orange":
				g2.setColor(Color.ORANGE);
			break;
			case "cyan":
				g2.setColor(Color.CYAN);
			break;
			case "gray":
				g2.setColor(Color.GRAY);
			break;
			default:
				g2.setColor(Color.WHITE);
			break;
		}
		if (!circular)
		{
			g2.fillRect(0, 0, img.getWidth(), img.getHeight());
		}
		else
		{
			g2.fillOval(0, 0, img.getWidth(), img.getHeight());
		}
		g2.dispose();
		return img;
	}
}
