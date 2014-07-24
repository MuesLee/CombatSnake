package timoschwarz.snake.model;

import java.awt.Color;
import java.awt.Image;

public interface WorldChanger
{

	public void modifyWorld(World world);

	public void restoreWorld(World world);

	public Color getColor();

	public Image getImage();

	@Override
	public String toString();

	public String getSoundFile();

}
