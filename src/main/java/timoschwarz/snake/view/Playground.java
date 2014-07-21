package timoschwarz.snake.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import timoschwarz.snake.controller.Controller;
import timoschwarz.snake.model.SnakePiece;

public class Playground extends JPanel
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

	private int width, height, frameCount = 0, fps = 0;
	private ArrayList<Entity> snakes = new ArrayList<Entity>();
	private Controller controller;
	private Random random = new Random();
	private ArrayList<Entity> looseSnakePieces;

	public Playground(Controller controller, int w, int h)
	{
		super(true);
		this.looseSnakePieces = new ArrayList<>();
		this.controller = controller;
		setIgnoreRepaint(true);
		setBackground(Color.black);
		width = w + BORDER_THICKNESS;
		height = h + BORDER_THICKNESS;
		setBorder(BorderFactory.createLineBorder(Color.RED, BORDER_THICKNESS));
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(width, height);
	}

	public void addEntity(Entity e)
	{
		snakes.add(e);
	}

	void clearEntities()
	{
		snakes.clear();
	}

	public void gameLoop()
	{
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
					updateGame(elapsedTime);
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES)
				{
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				drawGame();
				lastRenderTime = now;

				int thisSecond = (int) (lastUpdateTime / 1000000000);

				if (thisSecond > lastSecondTime)
				{
					fps = frameCount;
					frameCount = 0;
					lastSecondTime = thisSecond;
				}

				while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
					&& now - lastUpdateTime < TIME_BETWEEN_UPDATES)
				{
					Thread.yield();
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
		fps = 0;
	}

	private void drawGame()
	{
		repaint();
	}

	private void updateGame(long elapsedTime)
	{
		updateEntityMovements(elapsedTime);
	}

	private void updateEntityMovements(long elapsedTime)
	{
		for (Entity e : snakes)
		{
			e.update(elapsedTime);
			e.move();
		}

		for (Entity e : looseSnakePieces)
		{
			e.update(elapsedTime);
			e.move();
		}
	}

	@Override
	protected void paintComponent(Graphics grphcs)
	{
		super.paintComponent(grphcs);
		Graphics2D g2d = (Graphics2D) grphcs;

		applyRenderHints(g2d);
		drawBackground(g2d);
		drawEntitiesToScreen(g2d);
		drawFpsCounter(g2d);
		drawScore(g2d);

		frameCount++;
	}

	public static void applyRenderHints(Graphics2D g2d)
	{
		g2d.setRenderingHints(textRenderHints);
		g2d.setRenderingHints(imageRenderHints);
		g2d.setRenderingHints(colorRenderHints);
		g2d.setRenderingHints(interpolationRenderHints);
		g2d.setRenderingHints(renderHints);
	}

	private void drawEntitiesToScreen(Graphics2D g2d)
	{
		for (Entity e : snakes)
		{
			if (e.isVisible())
			{
				LinkedList<Rectangle2D.Double> rects = e.getRects();
				for (Rectangle2D.Double rec : rects)
				{
					g2d.draw(rec);
				}
			}
		}

		for (Entity e : getLooseSnakePieces())
		{
			if (e.isVisible())
			{
				LinkedList<Rectangle2D.Double> rects = e.getRects();
				for (Rectangle2D.Double rec : rects)
				{
					g2d.draw(rec);
				}
			}
		}
	}

	private void drawFpsCounter(Graphics2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.drawString("FPS: " + fps, 10, 15);
	}

	private void drawScore(Graphics2D g2d)
	{
		String namePlayerOne = controller.getPlayerOne().getName();
		String namePlayerTwo = controller.getPlayerTwo().getName();

		String scorePlayerOne = "" + controller.getPlayerOne().getPoints();
		String scorePlayerTwo = "" + controller.getPlayerTwo().getPoints();

		g2d.setColor(Color.white);
		g2d.drawString(namePlayerOne + " " + scorePlayerOne, 50, 15);
		g2d.drawString(namePlayerTwo + " " + scorePlayerTwo, 250, 15);
	}

	private void drawBackground(Graphics2D g2d)
	{
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		for (int i = 0; i < 128; i++)
		{
			g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
			g2d.drawLine(getWidth() / 2, getHeight() / 2, random.nextInt(getWidth()), random.nextInt(getHeight()));
		}
	}

	public Controller getController()
	{
		return controller;
	}

	public void setController(Controller controlller)
	{
		this.controller = controlller;
	}

	public void updateLooseSnakePieceEntities()
	{
		LinkedList<SnakePiece> looseSnakePieces2 = controller.getLooseSnakePieces();
		Entity entity = controller.createEntity(looseSnakePieces2, "yellow");
		looseSnakePieces.clear();
		looseSnakePieces.add(entity);
	}

	public ArrayList<Entity> getLooseSnakePieces()
	{
		return looseSnakePieces;
	}

	public void setLooseSnakePieces(ArrayList<Entity> looseSnakePieces)
	{
		this.looseSnakePieces = looseSnakePieces;
	}
}