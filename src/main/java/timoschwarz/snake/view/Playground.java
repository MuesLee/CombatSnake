package timoschwarz.snake.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.model.SnakePiece;
import timoschwarz.snake.util.EntityHelper;

public class Playground extends JPanel {

	private static final long serialVersionUID = -6340847313950500678L;
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

	public static AtomicBoolean running = new AtomicBoolean(false),
			paused = new AtomicBoolean(false);
	public static final int BORDER_THICKNESS = 5;

	private int width, height, frameCount = 0;
	private ArrayList<SnakeEntity> snakes = new ArrayList<SnakeEntity>();
	private GameController controller;
	private ArrayList<Entity> looseSnakePieces;

	private Image image_buffer;
	private Graphics graphics_buffer;

	public Playground(GameController controller, int w, int h) {
		super(true);
		this.looseSnakePieces = new ArrayList<>();
		this.controller = controller;
		setIgnoreRepaint(true);
		setBackground(Color.black);
		Insets insets = getInsets();
		width = w + 2 * BORDER_THICKNESS + insets.left + insets.right
				+ GameController.paintSize;
		height = h + 2 * BORDER_THICKNESS + insets.bottom + insets.top
				+ GameController.paintSize;
		setPreferredSize(getPreferredSize());
		setBorder(BorderFactory.createLineBorder(Color.RED, BORDER_THICKNESS));
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	public void addEntity(SnakeEntity e) {
		snakes.add(e);
	}

	void clearEntities() {
		snakes.clear();
	}

	void updateRectsInSnakeEntities() {
		for (int i = 0; i < snakes.size(); i++) {
			snakes.get(i).checkForNewSnakePieces();
		}
	}

	public void gameLoop() {
		final double GAME_HERTZ = 60.0;

		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		final int MAX_UPDATES_BEFORE_RENDER = 5;

		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();

		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		long currTime = System.currentTimeMillis();

		while (running.get()) {
			if (!paused.get()) {

				double now = System.nanoTime();
				long elapsedTime = System.currentTimeMillis() - currTime;
				currTime += elapsedTime;
				int updateCount = 0;

				while (now - lastUpdateTime > TIME_BETWEEN_UPDATES
						&& updateCount < MAX_UPDATES_BEFORE_RENDER) {
					updateGame(elapsedTime);
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				drawGame();
				lastRenderTime = now;

				int thisSecond = (int) (lastUpdateTime / 1000000000);

				if (thisSecond > lastSecondTime) {
					frameCount = 0;
					lastSecondTime = thisSecond;
				}

				while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
						&& now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
					Thread.yield();
					try {
						Thread.sleep(1);
					} catch (Exception e) {
					}
					now = System.nanoTime();
				}
			}
		}
	}

	private void drawGame() {
		repaint();
	}

	private void updateGame(long elapsedTime) {
		updateEntityMovements(elapsedTime);
	}

	private void updateEntityMovements(long elapsedTime) {
		updateRectsInSnakeEntities();
		for (SnakeEntity e : snakes) {
			e.update(elapsedTime);
			e.move();
		}

		for (Entity e : looseSnakePieces) {
			e.update(elapsedTime);
			e.move();
		}
	}

	@Override
	protected void paintComponent(Graphics grphcs) {
		super.paintComponent(grphcs);
		Graphics2D g2d = (Graphics2D) grphcs;

		// applyRenderHints(g2d);
		drawEntitiesToScreen(g2d);
		frameCount++;
	}

	public static void applyRenderHints(Graphics2D g2d) {
		g2d.setRenderingHints(textRenderHints);
		g2d.setRenderingHints(imageRenderHints);
		g2d.setRenderingHints(colorRenderHints);
		g2d.setRenderingHints(interpolationRenderHints);
		g2d.setRenderingHints(renderHints);
	}

	private void drawEntitiesToScreen(Graphics2D g2d) {
		if (image_buffer == null) {
			image_buffer = createImage(getWidth(), getHeight());
			graphics_buffer = image_buffer.getGraphics();
		}

		graphics_buffer.setColor(getBackground());
		graphics_buffer.fillRect(0, 0, getWidth(), getHeight());

		for (int i = 0; i < snakes.size(); i++) {
			Color c = getColorForSnake(i);
			graphics_buffer.setColor(c);

			Entity e = snakes.get(i);
			if (e.isVisible()) {
				LinkedList<Rectangle2D.Double> rects = e.getRects();
				for (Rectangle2D.Double rec : rects) {
					graphics_buffer.fillRect((int) rec.x, (int) rec.y,
							(int) rec.width, (int) rec.height);
				}
			}
		}

		for (Entity e : getLooseSnakePieces()) {
			graphics_buffer.setColor(Color.RED);
			if (e.isVisible()) {
				LinkedList<Rectangle2D.Double> rects = e.getRects();
				for (Rectangle2D.Double rec : rects) {
					graphics_buffer.fillRect((int) rec.x, (int) rec.y,
							(int) rec.width, (int) rec.height);
				}
			}
		}

		g2d.drawImage(image_buffer, 0, 0, this);
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
		LinkedList<SnakePiece> looseSnakePieces2 = controller
				.getLooseSnakePieces();
		Entity entity = EntityHelper.createEntity(looseSnakePieces2, "yellow");
		looseSnakePieces.clear();
		looseSnakePieces.add(entity);
	}

	public ArrayList<Entity> getLooseSnakePieces() {
		return looseSnakePieces;
	}

	public void setLooseSnakePieces(ArrayList<Entity> looseSnakePieces) {
		this.looseSnakePieces = looseSnakePieces;
	}
}