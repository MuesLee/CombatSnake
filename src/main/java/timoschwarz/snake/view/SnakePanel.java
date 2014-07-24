package timoschwarz.snake.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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
	private Image overlayImage;
	private Image backgroundImage;

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
		overlayImage = createOverlayImage();
		backgroundImage = createBackGroundImage();
	}

	public void paintWorldChangerSpawn(Graphics g)
	{
		g.drawImage(overlayImage, 0, 0, null);
		g.drawImage(backgroundImage, 0, 0, null);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(width, height);
	}

	private BufferedImage createBackGroundImage()
	{
		int PREF_W = 50;
		int PREF_H = 50;

		BufferedImage img = new BufferedImage(PREF_W, PREF_H, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(6f));
		g2.setColor(Color.blue);
		int circleCount = 10;
		for (int i = 0; i < circleCount; i++)
		{
			int x = i * PREF_W / (2 * circleCount);
			int y = x;
			int w = PREF_W - 2 * x;
			int h = w;
			g2.drawOval(x, y, w, h);
		}
		g2.dispose();
		return img;
	}

	private BufferedImage createOverlayImage()
	{
		int PREF_W = 50;
		int PREF_H = 50;
		BufferedImage img = new BufferedImage(PREF_W, PREF_H, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.red);
		g2.setStroke(new BasicStroke(6f));
		int circleCount = 10;
		for (int i = 0; i < circleCount + 1; i++)
		{
			int x1 = i * PREF_W / circleCount;
			int y1 = 0;
			int x2 = PREF_W - x1;
			int y2 = PREF_H;
			float alpha = (float) i / circleCount;
			if (alpha > 1f)
			{
				alpha = 1f;
			}
			// int rule = AlphaComposite.CLEAR;
			int rule = AlphaComposite.SRC_OVER;
			Composite comp = AlphaComposite.getInstance(rule, alpha);
			g2.setComposite(comp);
			g2.drawLine(x1, y1, x2, y2);
		}
		g2.dispose();
		return img;
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
		paintWorldChangerSpawn(g);
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