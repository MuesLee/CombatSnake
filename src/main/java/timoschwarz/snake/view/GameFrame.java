package timoschwarz.snake.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import timoschwarz.snake.configurator.GameConfigurator;
import timoschwarz.snake.controller.GameController;

public class GameFrame extends JFrame implements ClockListener
{

	private static final long serialVersionUID = -6931296138340412107L;
	private GameController controller;
	private SnakePanel snakePanel;
	private Clock clock;
	private JMenuBar menubar;

	public GameFrame(String s, GameController controller)
	{
		this.controller = controller;
		setTitle(s);
		configure();
		getContentPane().setBackground(Color.black);
	}

	private void configure()
	{
		JMenu startMenu = new JMenu("Start");
		JMenuItem configureNewGameItem = new JMenuItem("New Game...");
		configureNewGameItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GameConfigurator gameConfigurator = new GameConfigurator();
				dispose();
			}
		});
		JMenu helpMenu = new JMenu("Help");

		startMenu.add(configureNewGameItem);

		menubar = new JMenuBar();
		menubar.add(startMenu);
		menubar.add(helpMenu);
		setJMenuBar(menubar);
	}

	public Clock getClock()
	{
		return clock;
	}

	public void setClock(Clock clk)
	{
		if (this.clock != null)
		{
			this.clock.removeClockListener(this);
		}

		this.clock = clk;
		this.clock.addClockListener(this);
	}

	@Override
	public void tick()
	{
		repaint();
	}

	@Override
	public void paintComponents(Graphics g)
	{
		g.setColor(Color.black);

		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponents(g);
	}

	public SnakePanel getSnakePanel()
	{
		return snakePanel;
	}

	public void setSnakePanel(SnakePanel snakePanel)
	{
		this.snakePanel = snakePanel;
	}
}
