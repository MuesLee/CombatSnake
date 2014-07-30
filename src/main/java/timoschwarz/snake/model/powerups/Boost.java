package timoschwarz.snake.model.powerups;

import java.awt.Color;
import java.awt.Image;

import timoschwarz.snake.model.Snake;

public interface Boost
{
	public void modifySnake(Snake snake);

	public Color getColor();

	public Image getImage();

	public void restoreSnake(Snake snake);

	@Override
	public String toString();

	public String getSoundFileName();
}
