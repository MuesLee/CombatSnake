package timoschwarz.snake.main;

import javax.swing.UIManager;

import timoschwarz.snake.configurator.GameConfigurator;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		GameConfigurator gc = new GameConfigurator();

	}
}
