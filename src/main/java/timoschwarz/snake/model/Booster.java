package timoschwarz.snake.model;

import java.awt.Color;
import java.awt.Image;

public interface Booster
{
	public void modifySnake(Snake snake);

	public Color getColor();

	public Image getImage();
}
