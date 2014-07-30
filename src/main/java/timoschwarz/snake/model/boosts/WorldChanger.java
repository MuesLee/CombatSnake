package timoschwarz.snake.model.boosts;

import java.awt.Color;
import java.awt.Image;

import timoschwarz.snake.model.World;

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
