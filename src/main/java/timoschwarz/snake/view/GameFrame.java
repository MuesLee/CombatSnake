package timoschwarz.snake.view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class GameFrame extends JFrame implements ClockListener
{

	private static final long serialVersionUID = -6931296138340412107L;
	private SnakePanel snakePanel;
	private Clock clock;

	public GameFrame(String s)
	{
		setTitle(s);
		getContentPane().setBackground(Color.black);
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
