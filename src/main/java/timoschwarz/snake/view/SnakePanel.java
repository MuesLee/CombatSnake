package timoschwarz.snake.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import timoschwarz.snake.controller.GameController;

public class SnakePanel extends JPanel
{

	private static final long serialVersionUID = -6340847313950500678L;
	private final static RenderingHints textRenderHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	private final static RenderingHints imageRenderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	private final static RenderingHints colorRenderHints = new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,
		RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	private final static RenderingHints interpolationRenderHints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	private final static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);

	public static AtomicBoolean running = new AtomicBoolean(false), paused = new AtomicBoolean(false);
	public static final int BORDER_THICKNESS = 5;

	private int width, height, fps, frameCount;

	private SnakeCanvas canvas;

	public SnakePanel(GameController controller, int w, int h, int paintSize)
	{
		super(true);
		setBackground(Color.black);
		setLayout(new BorderLayout());
		Insets insets = getInsets();
		width = w + 2 * BORDER_THICKNESS + insets.left + insets.right + GameController.paintSize;
		height = h + 2 * BORDER_THICKNESS + insets.bottom + insets.top + GameController.paintSize;
		setCanvas(new SnakeCanvas(controller, this, width, height, paintSize));
		setPreferredSize(getPreferredSize());
		setBorder(BorderFactory.createLineBorder(Color.RED, BORDER_THICKNESS));
		add(canvas, BorderLayout.CENTER);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(width, height);
	}

	public void gameLoop()
	{
		canvas.createBufferStrategy(3);

		final double GAME_HERTZ = 60.0;

		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		final int MAX_UPDATES_BEFORE_RENDER = 5;

		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();

		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		long currTime = System.currentTimeMillis();

		while (running.get())
		{
			if (!paused.get())
			{

				double now = System.nanoTime();
				long elapsedTime = System.currentTimeMillis() - currTime;
				currTime += elapsedTime;
				int updateCount = 0;

				while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER)
				{
					repaint();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES)
				{
					setFps(frameCount);
					frameCount = 0;
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				drawGame();
				lastRenderTime = now;

				int thisSecond = (int) (lastUpdateTime / 1000000000);

				if (thisSecond > lastSecondTime)
				{
					lastSecondTime = thisSecond;
				}

				while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
					&& now - lastUpdateTime < TIME_BETWEEN_UPDATES)
				{
					try
					{
						Thread.sleep(1);
					}
					catch (Exception e)
					{
					}
					now = System.nanoTime();
				}
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		frameCount++;
	}

	private void drawGame()
	{
		canvas.repaint();
	}

	public SnakeCanvas getCanvas()
	{
		return canvas;
	}

	public void setCanvas(SnakeCanvas canvas)
	{
		this.canvas = canvas;
	}

	public int getFps()
	{
		return fps;
	}

	public void setFps(int fps)
	{
		this.fps = fps;
	}

}