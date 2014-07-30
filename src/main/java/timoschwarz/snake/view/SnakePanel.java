package timoschwarz.snake.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.model.Boost;
import timoschwarz.snake.model.Coordinates;
import timoschwarz.snake.model.Piece;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;
import timoschwarz.snake.model.WorldChanger;

public class SnakePanel extends JPanel
{

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

	private int paintSize;

	private GameController controller;

	private List<Snake> snakes;
	private List<Piece> looseSnakePieces;
	private List<Boost> booster;
	private List<WorldChanger> worldChangers;

	private Image backBuffer;
	private Graphics bBG;

	private Random random = new Random();
	private List<Lightning> lightnings = new ArrayList<Lightning>();

	public static AtomicBoolean running = new AtomicBoolean(false), paused = new AtomicBoolean(false);
	public static final int BORDER_THICKNESS = 5;

	private int width, height;

	public SnakePanel(GameController controller, int w, int h, int paintSize)
	{
		super(true);
		Insets insets = getInsets();
		width = w + 2 * BORDER_THICKNESS + insets.left + insets.right + paintSize;
		height = h + 2 * BORDER_THICKNESS + insets.bottom + insets.top + paintSize;
		setPreferredSize(getPreferredSize());
		setMinimumSize(getPreferredSize());
		setBorder(BorderFactory.createLineBorder(Color.RED, BORDER_THICKNESS));
		this.paintSize = paintSize;
		this.controller = controller;
		setIgnoreRepaint(true);
		this.setBooster(new ArrayList<Boost>());
		this.looseSnakePieces = new ArrayList<Piece>();
		this.setSnakes(new ArrayList<Snake>());
		this.setWorldChangers(new ArrayList<WorldChanger>());
		setBackground(Color.black);
		setVisible(true);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(width, height);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		if (backBuffer == null)
		{
			backBuffer = createImage(getWidth(), getHeight());
			bBG = backBuffer.getGraphics();
		}

		paintEntities(bBG);

		g.drawImage(backBuffer, BORDER_THICKNESS, BORDER_THICKNESS, this);
	}

	public static void applyRenderHints(Graphics2D g2d)
	{
		g2d.setRenderingHints(textRenderHints);
		g2d.setRenderingHints(imageRenderHints);
		g2d.setRenderingHints(colorRenderHints);
		g2d.setRenderingHints(interpolationRenderHints);
		g2d.setRenderingHints(renderHints);
	}

	public void paintEntities(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		applyRenderHints(g2d);
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		drawEntitiesToScreen(g2d);
		drawLightning(g2d);
		drawFPS(g2d);
	}

	private void drawEntitiesToScreen(Graphics2D graphics)
	{
		for (int i = 0; i < getSnakes().size(); i++)
		{
			Color c = getColorForSnake(i);
			graphics.setColor(c);

			Snake e = getSnakes().get(i);
			boolean isPhased = e.isPhased();
			LinkedList<SnakePiece> pieces = e.getSnakePieces();

			for (SnakePiece piece : pieces)
			{
				if (e.getHead() == piece && isPhased)
				{

					graphics.setColor(Color.blue);
					graphics.fillRect(piece.getX() * paintSize, piece.getY() * paintSize, paintSize, paintSize);
					graphics.setColor(c);
				}
				else
				{
					graphics.fillRect(piece.getX() * paintSize, piece.getY() * paintSize, paintSize, paintSize);
				}
			}
		}

		for (Piece e : looseSnakePieces)
		{
			graphics.setColor(Color.RED);
			graphics.fillRect(e.getX() * paintSize, e.getY() * paintSize, paintSize, paintSize);
		}

		for (Boost boost : getBooster())
		{
			Piece e = (Piece) boost;
			graphics.setColor(boost.getColor());
			graphics.fillRect(e.getX() * paintSize, e.getY() * paintSize, paintSize, paintSize);
		}

		for (WorldChanger worldChanger : getWorldChangers())
		{
			Piece e = (Piece) worldChanger;
			graphics.setColor(worldChanger.getColor());
			graphics.fillRect(e.getX() * paintSize, e.getY() * paintSize, paintSize, paintSize);
		}

	}

	private void drawLightning(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.YELLOW);

		for (Lightning lightning : lightnings)
		{
			if (lightning.isDone())
			{
				continue;
			}

			List<LightningSegment> segments = lightning.getSegments();

			for (LightningSegment lightningSegment : segments)
			{

				g2d.setStroke(new BasicStroke(3F));

				final Coordinates start = lightningSegment.getStart();
				final Coordinates end = lightningSegment.getEnd();

				if (lightningSegment.isTrunk())
				{
					g2d.setStroke(new BasicStroke(6F));
				}
				g2d.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
			}

			lightning.increaseFrameCount();
			if (lightning.getFrameCount() % 10 == 0)
			{
				lightning.nextGeneration();
			}
		}
	}

	private Color getColorForSnake(int i)
	{
		switch (i)
		{
			case 0:
				return Color.WHITE;
			case 1:
				return Color.GRAY;

			default:
				return Color.YELLOW;
		}
	}

	public GameController getController()
	{
		return controller;
	}

	public void setController(GameController controlller)
	{
		this.controller = controlller;
	}

	public void updateLooseSnakePieceEntities()
	{
		looseSnakePieces = controller.getLooseSnakePieces();
	}

	public void updateWorldChangerEntities()
	{
		worldChangers = controller.getCurrentWorldChangers();
	}

	public List<Piece> getLooseSnakePieces()
	{
		return looseSnakePieces;
	}

	public void setLooseSnakePieces(List<Piece> looseSnakePieces)
	{
		this.looseSnakePieces = looseSnakePieces;
	}

	public void updateBooster(List<Boost> currentBooster)
	{
		this.setBooster(currentBooster);
	}

	public void clearBooster()
	{
		getBooster().clear();

	}

	public List<Snake> getSnakes()
	{
		return snakes;
	}

	public void setSnakes(List<Snake> snakes)
	{
		this.snakes = snakes;
	}

	public List<Boost> getBooster()
	{
		return booster;
	}

	public void setBooster(List<Boost> booster)
	{
		this.booster = booster;
	}

	public List<WorldChanger> getWorldChangers()
	{
		return worldChangers;
	}

	public void setWorldChangers(List<WorldChanger> worldChangers)
	{
		this.worldChangers = worldChangers;
	}

	public void addLightning(int x, int y)
	{
		Lightning seg = new Lightning(new Coordinates(x, 0), new Coordinates(x, y));
		lightnings.add(seg);
	}

	public void clearLightnings()
	{
		lightnings.clear();
	}

	public void addRandomLightning()
	{
		int y = random.nextInt(height);
		int x = random.nextInt(width);
		addLightning(x, y);
	}

	private void drawFPS(Graphics g)
	{
		g.setColor(Color.GREEN);
		g.drawString("FPS: " + GameController.REAL_FPS, BORDER_THICKNESS, BORDER_THICKNESS + 5);
	}

	void drawGame()
	{
		repaint();
	}
}