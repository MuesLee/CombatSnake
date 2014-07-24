package timoschwarz.snake.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;

public class GraphicsController
{

	private Properties prop;

	public GraphicsController()
	{
		loadProperties();
	}

	private void loadProperties()
	{
		prop = new Properties();
		try
		{
			prop.load(ClassLoader.getSystemResourceAsStream("graphics.properties"));
		}
		catch (IOException e)
		{
			System.out.println("Properties konnte nicht geladen werden");
		}
	}

	public BufferedImage createLightningImage()
	{
		URL systemResourceAsStream = ClassLoader.getSystemResource(prop.getProperty("lightning"));
		BufferedImage read = null;
		try
		{
			read = ImageIO.read(systemResourceAsStream);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return read;
	}
}
