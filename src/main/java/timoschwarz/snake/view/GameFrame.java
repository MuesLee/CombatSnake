package timoschwarz.snake.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import timoschwarz.snake.configurator.GameConfigurator;
import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.dao.Score;

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
	}

	private void configure()
	{
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setBackground(Color.black);
		JMenu startMenu = new JMenu("Start");
		JMenuItem configureNewGameItem = new JMenuItem("New Game...");
		JMenuItem exitItem = new JMenuItem("Exit");

		exitItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
		});
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

		JMenuItem showHighscoreItem = new JMenuItem("Highscores");
		showHighscoreItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JFrame scoreFrame = new JFrame("Highscore");
				scoreFrame.setLayout(new BorderLayout());
				scoreFrame.setSize(200, 500);
				scoreFrame.setLocationRelativeTo(null);
				scoreFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				scoreFrame.setVisible(true);
				List<Score> highscore = controller.getHighscore();
				JList scores = new JList<>(highscore.toArray());
				scores.setVisibleRowCount(10);
				scoreFrame.add(scores, BorderLayout.CENTER);
			}
		});
		helpMenu.add(showHighscoreItem);

		startMenu.add(configureNewGameItem);
		startMenu.add(exitItem);

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

	public void clearAll()
	{
		removeAll();
	}
}
