package timoschwarz.snake.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.model.Coordinates;
import timoschwarz.snake.model.Piece;
import timoschwarz.snake.model.Snake;
import timoschwarz.snake.model.SnakePiece;
import timoschwarz.snake.model.powerups.Boost;
import timoschwarz.snake.model.powerups.WorldChanger;
import timoschwarz.snake.util.VideoUtils;

public class SnakePanel extends JPanel {

	private final static RenderingHints textRenderHints = new RenderingHints(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	private final static RenderingHints imageRenderHints = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	private final static RenderingHints colorRenderHints = new RenderingHints(
			RenderingHints.KEY_COLOR_RENDERING,
			RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	private final static RenderingHints interpolationRenderHints = new RenderingHints(
			RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	private final static RenderingHints renderHints = new RenderingHints(
			RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

	private int paintSize;

	private GameController controller;

	private volatile List<Snake> snakes;
	private volatile List<Piece> looseSnakePieces;
	private volatile List<Boost> booster;
	private volatile List<WorldChanger> worldChangers;

	private Image backBuffer;
	private Graphics bBG;

	private Random random = new Random();
	private List<Lightning> lightnings = new ArrayList<Lightning>();

	public static AtomicBoolean running = new AtomicBoolean(false),
			paused = new AtomicBoolean(false);
	public static final int BORDER_THICKNESS = 5;
	private static int SPACE_LEFTRIGHT;
	private static int SPACE_TOPBOT;

	private int width, height;

	private int worldWidth, worldHeight;

	public SnakePanel(GameController controller, int w, int h, int paintSize) {
		super(true);
		initSideSpaces();
		this.setPaintSize(paintSize);
		this.controller = controller;
		this.setBooster(new ArrayList<Boost>());
		this.looseSnakePieces = new ArrayList<Piece>();
		this.setSnakes(new ArrayList<Snake>());
		this.setWorldChangers(new ArrayList<WorldChanger>());

		setWorldWidth(w);
		setWorldHeight(h);

		setWidth(calculatePanelWidth());
		setHeight(calculatePanelHeight());

		setPreferredSize(getPreferredSize());
		setMinimumSize(getPreferredSize());
		setIgnoreRepaint(true);
		setBackground(Color.black);
		setVisible(true);
	}

	private int calculatePanelHeight() {
		Insets insets = getInsets();

		return getWorldHeight() * paintSize + 2 * BORDER_THICKNESS
				+ insets.bottom + insets.top + paintSize + 2 * SPACE_LEFTRIGHT
				+ 2 * SPACE_TOPBOT;
	}

	private int calculatePanelWidth() {
		Insets insets = getInsets();

		return getWorldWidth() * paintSize + 2 * BORDER_THICKNESS + insets.left
				+ insets.right + paintSize + 2 * SPACE_LEFTRIGHT + 2
				* SPACE_TOPBOT;

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (backBuffer == null) {
			backBuffer = createImage(getWidth(), getHeight());
			bBG = backBuffer.getGraphics();
		}

		paintEntities(bBG);
		g.drawImage(backBuffer, 0, 0, this);
	}

	public static void applyRenderHints(Graphics2D g2d) {
		g2d.setRenderingHints(textRenderHints);
		g2d.setRenderingHints(imageRenderHints);
		g2d.setRenderingHints(colorRenderHints);
		g2d.setRenderingHints(interpolationRenderHints);
		g2d.setRenderingHints(renderHints);
	}

	public void paintEntities(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		applyRenderHints(g2d);
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		drawEntitiesToScreen(g2d);
		drawBorders(g2d);
		drawLightning(g2d);
		drawFPS(g2d);
	}

	private void drawBorders(Graphics2D graphics) {
		graphics.setColor(Color.MAGENTA);

		// TOP
		graphics.fillRect(SPACE_LEFTRIGHT - BORDER_THICKNESS, SPACE_TOPBOT
				- BORDER_THICKNESS, paintSize + worldWidth * paintSize + 2
				* BORDER_THICKNESS, BORDER_THICKNESS);
		// BOT
		graphics.fillRect(SPACE_LEFTRIGHT - BORDER_THICKNESS, SPACE_TOPBOT
				+ worldHeight * paintSize + paintSize, paintSize + worldWidth
				* paintSize + 2 * BORDER_THICKNESS, BORDER_THICKNESS);
		// RIGHT
		graphics.fillRect(paintSize + SPACE_LEFTRIGHT + worldWidth * paintSize,
				SPACE_TOPBOT - BORDER_THICKNESS, BORDER_THICKNESS, paintSize
						+ worldHeight * paintSize + 2 * BORDER_THICKNESS);
		// LEFT
		graphics.fillRect(SPACE_LEFTRIGHT - BORDER_THICKNESS, SPACE_TOPBOT
				- BORDER_THICKNESS, BORDER_THICKNESS, paintSize + worldHeight
				* paintSize + 2 * BORDER_THICKNESS);
	}

	private void drawEntitiesToScreen(Graphics2D graphics) {
		for (int i = 0; i < getSnakes().size(); i++) {
			Color c = getColorForSnake(i);
			graphics.setColor(c);

			Snake e = getSnakes().get(i);
			boolean isPhased = e.isPhased();
			LinkedList<SnakePiece> pieces = e.getSnakePieces();

			for (SnakePiece piece : pieces) {
				if (e.getHead() == piece && isPhased) {

					graphics.setColor(Color.blue);
					graphics.fillRect(getShiftedXCoordForPiece(piece),
							getShiftedYCoordForPiece(piece), getPaintSize(),
							getPaintSize());
					graphics.setColor(c);
				} else {
					graphics.fillRect(getShiftedXCoordForPiece(piece),
							getShiftedYCoordForPiece(piece), getPaintSize(),
							getPaintSize());
				}
			}
		}

		for (Piece e : looseSnakePieces) {
			graphics.setColor(Color.RED);
			graphics.fillRect(getShiftedXCoordForPiece(e),
					getShiftedYCoordForPiece(e), getPaintSize(), getPaintSize());
		}

		for (Boost boost : getBooster()) {
			Piece e = (Piece) boost;
			graphics.setColor(boost.getColor());
			graphics.fillRect(getShiftedXCoordForPiece(e),
					getShiftedYCoordForPiece(e), getPaintSize(), getPaintSize());
		}

		for (WorldChanger worldChanger : getWorldChangers()) {
			Piece e = (Piece) worldChanger;
			graphics.setColor(worldChanger.getColor());
			graphics.fillRect(getShiftedXCoordForPiece(e),
					getShiftedYCoordForPiece(e), getPaintSize(), getPaintSize());
		}

	}

	private int getShiftedXCoordForPiece(Piece piece) {
		return piece.getX() * paintSize + SPACE_LEFTRIGHT;
	}

	private int getShiftedYCoordForPiece(Piece piece) {
		return piece.getY() * paintSize + SPACE_TOPBOT;
	}

	private void drawLightning(Graphics g) {
		if (lightnings.isEmpty()) {
			return;
		}

		Graphics2D g2d = (Graphics2D) g;

		final Stroke bigStroke = new BasicStroke(6F);
		final Stroke smallStroke = new BasicStroke(3F);

		final Color mainColor = new Color(255, 243, 60);
		final Color shadeColor = new Color(255, 250, 168);

		for (Lightning lightning : lightnings) {
			if (lightning.isDone()) {
				continue;
			}

			List<LightningSegment> segments = lightning.getSegments();

			for (LightningSegment lightningSegment : segments) {

				g2d.setStroke(smallStroke);

				final Coordinates start = lightningSegment.getStart();
				final Coordinates end = lightningSegment.getEnd();

				if (lightningSegment.isTrunk()) {
					g2d.setStroke(bigStroke);
				}
				g2d.setColor(mainColor);
				g2d.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
				g2d.setColor(shadeColor);
				g2d.drawLine(start.getX() + 2, start.getY(), end.getX() + 2,
						end.getY());
			}

			lightning.increaseFrameCount();
			if (lightning.getFrameCount() % 10 == 0) {
				lightning.nextGeneration();
			}
		}
	}

	private Color getColorForSnake(int i) {
		switch (i) {
		case 0:
			return Color.WHITE;
		case 1:
			return Color.GRAY;

		default:
			return Color.YELLOW;
		}
	}

	public GameController getController() {
		return controller;
	}

	public void setController(GameController controlller) {
		this.controller = controlller;
	}

	public void updateLooseSnakePieceEntities() {
		looseSnakePieces = controller.getLooseSnakePieces();
	}

	public void updateWorldChangerEntities() {
		worldChangers = controller.getCurrentWorldChangers();
	}

	public List<Piece> getLooseSnakePieces() {
		return looseSnakePieces;
	}

	public void setLooseSnakePieces(List<Piece> looseSnakePieces) {
		this.looseSnakePieces = looseSnakePieces;
	}

	public void updateBooster(List<Boost> currentBooster) {
		this.setBooster(currentBooster);
	}

	public void clearBooster() {
		getBooster().clear();

	}

	public List<Snake> getSnakes() {
		return snakes;
	}

	public void setSnakes(List<Snake> snakes) {
		this.snakes = snakes;
	}

	public List<Boost> getBooster() {
		return booster;
	}

	public void setBooster(List<Boost> booster) {
		this.booster = booster;
	}

	public List<WorldChanger> getWorldChangers() {
		return worldChangers;
	}

	public void setWorldChangers(List<WorldChanger> worldChangers) {
		this.worldChangers = worldChangers;
	}

	public void addLightning(int x, int y) {
		Lightning seg = new Lightning(new Coordinates(x, 0), new Coordinates(x,
				y));
		lightnings.add(seg);
	}

	public void clearLightnings() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				boolean allLightningsWereAnimated = true;

				while (!allLightningsWereAnimated) {

					for (Lightning l : lightnings) {
						if (!l.isDone()) {
							allLightningsWereAnimated = false;
						}
					}
					if (allLightningsWereAnimated) {
						lightnings.clear();
					} else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});

		t.start();

	}

	public void addRandomLightning() {
		int y = random.nextInt(getHeight());
		int x = random.nextInt(getWidth());
		addLightning(x, y);
	}

	private void drawFPS(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawString("FPS: " + GameController.REAL_FPS, BORDER_THICKNESS,
				BORDER_THICKNESS + 5);
	}

	void drawGame() {
		repaint();
	}

	public int getPaintSize() {
		return paintSize;
	}

	public void setPaintSize(int paintSize) {
		this.paintSize = paintSize;
	}

	@Override
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public void setWorldHeight(int worldHeight) {
		this.worldHeight = worldHeight;
	}

	public int getWorldWidth() {
		return worldWidth;
	}

	public void setWorldWidth(int worldWidth) {
		this.worldWidth = worldWidth;
	}

	public void updateSize() {
		this.width = calculatePanelWidth();
		this.height = calculatePanelHeight();
		updateSideSpaces();
		repaint();
	}

	private void updateSideSpaces() {
		int paintedAreaWidth = getWorldWidth() * paintSize + 2
				* BORDER_THICKNESS + paintSize;
		int paintedAreaHeigth = getWorldWidth() * paintSize + 2
				* BORDER_THICKNESS + paintSize;

		int panelWidth = getWidth();
		int panelHeight = getWidth();

		SPACE_LEFTRIGHT = panelWidth - paintedAreaWidth;
		SPACE_LEFTRIGHT /= 2;
		SPACE_TOPBOT = panelHeight - paintedAreaHeigth;
		SPACE_TOPBOT /= 2;

	}

	private void initSideSpaces() {
		SPACE_LEFTRIGHT = (int) (VideoUtils.getScreenWidth() * 0.05);
		SPACE_TOPBOT = (int) (VideoUtils.getScreenHeight() * 0.01);
	}

}