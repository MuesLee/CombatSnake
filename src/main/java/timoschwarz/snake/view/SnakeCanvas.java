package timoschwarz.snake.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.model.Boost;
import timoschwarz.snake.model.Piece;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;
import timoschwarz.snake.model.WorldChanger;

public class SnakeCanvas extends Canvas
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
	private SnakePanel panel;
	private int width, height;

	private GameController controller;

	private List<Snake> snakes;
	private List<Piece> looseSnakePieces;
	private List<Boost> booster;
	private List<WorldChanger> worldChangers;

	private Image backBuffer;
	private Graphics bBG;
	private BufferedImage overlayImage;
	private boolean paintWorldChangerEvent = false;

	public SnakeCanvas(GameController controller, SnakePanel panel, int width, int height, int paintSize)
	{
		this.paintSize = paintSize;
		this.panel = panel;
		this.width = width;
		this.height = height;
		this.controller = controller;

		this.setBooster(new ArrayList<Boost>());
		this.looseSnakePieces = new ArrayList<Piece>();
		this.setSnakes(new ArrayList<Snake>());
		this.setWorldChangers(new ArrayList<WorldChanger>());
		this.overlayImage = controller.createOverlayImage();
		setBackground(Color.black);
		setVisible(true);
	}

	@Override
	public void update(Graphics g)
	{
		paint(g);
	}

	@Override
	public void paint(Graphics g)
	{
		if (backBuffer == null)
		{
			backBuffer = createImage(getWidth(), getHeight());
			bBG = backBuffer.getGraphics();
		}

		paintEntities(bBG);

		g.drawImage(backBuffer, 0, 0, this);
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
		g2d.clearRect(0, 0, width, height);
		drawEntitiesToScreen(g2d);
		//paintWorldChangerSpawn(g2d);
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

	public void paintWorldChangerSpawn(Graphics g)
	{
		if (isPaintWorldChangerEvent())
		{
			g.drawImage(overlayImage, 0, 0, null);
		}
	}

	public boolean isPaintWorldChangerEvent()
	{
		return paintWorldChangerEvent;
	}

	public void setPaintWorldChangerEvent(boolean paintWorldChangerEvent)
	{
		this.paintWorldChangerEvent = paintWorldChangerEvent;
	}

}
