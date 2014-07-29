package timoschwarz.snake.view;

import javax.swing.JFrame;

public class GameFrame extends JFrame implements ClockListener
{

	private static final long serialVersionUID = -6931296138340412107L;
	private SnakePanel snakePanel;
	private Clock clock;

	public GameFrame(String s)
	{
		setTitle(s);
		setIgnoreRepaint(true);
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

	public SnakePanel getSnakePanel()
	{
		return snakePanel;
	}

	public void setSnakePanel(SnakePanel snakePanel)
	{
		this.snakePanel = snakePanel;
	}
}
