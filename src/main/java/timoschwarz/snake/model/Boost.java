package timoschwarz.snake.model;

import java.awt.Color;
import java.awt.Image;

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
