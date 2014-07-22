package timoschwarz.snake.main;

import javax.swing.UIManager;

import timoschwarz.snake.controller.Controller;

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

		Controller controller = new Controller();

	}
}
