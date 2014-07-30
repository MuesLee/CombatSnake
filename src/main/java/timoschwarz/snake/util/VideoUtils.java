package timoschwarz.snake.util;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class VideoUtils
{

	public static int getScreenWidth()
	{
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return gd.getDisplayMode().getWidth();

	}

	public static int getScreenHeight()
	{
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return gd.getDisplayMode().getHeight();
	}

}
