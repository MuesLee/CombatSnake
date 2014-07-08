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

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Playground extends JPanel
{

	/**
	 * 
	 */
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
	private int width, height, frameCount = 0, fps = 0;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private final Random random = new Random();

	public Playground(int w, int h)
	{
		super(true);//make sure double buffering is enabled
		setIgnoreRepaint(true);//mustnt repaint itself the gameloop will do that
		width = w;
		height = h;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(width, height);
	}

	public void addEntity(Entity e)
	{
		entities.add(e);
	}

	void clearEntities()
	{
		entities.clear();
	}

	//Only run this in another Thread!
	public void gameLoop()
	{
		System.out.println("Loop");

		//This value would probably be stored elsewhere.
		final double GAME_HERTZ = 30.0;
		//Calculate how many ns each frame should take for our target game hertz.
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		//At the very most we will update the game this many times before a new render.
		//If you're worried about visual hitches more than perfect timing, set this to 1.
		final int MAX_UPDATES_BEFORE_RENDER = 5;
		//We will need the last update time.
		double lastUpdateTime = System.nanoTime();
		//Store the last time we rendered.
		double lastRenderTime = System.nanoTime();

		//If we are able to get as high as this FPS, don't render again.
		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

		//Simple way of finding FPS.
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);
		//store the time we started this will be used for updating map and charcter animations
		long currTime = System.currentTimeMillis();

		while (running.get())
		{
			if (!paused.get())
			{

				double now = System.nanoTime();
				long elapsedTime = System.currentTimeMillis() - currTime;
				currTime += elapsedTime;
				int updateCount = 0;

				//Do as many game updates as we need to, potentially playing catchup.
				while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER)
				{

					updateGame(elapsedTime);//Update the entity movements and collision checks etc (all has to do with updating the games status i.e  call move() on Enitites)

					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				//If for some reason an update takes forever, we don't want to do an insane number of catchups.
				//If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
				if (now - lastUpdateTime > TIME_BETWEEN_UPDATES)
				{
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				drawGame();//draw the game by invokeing repaint (which will call paintComponent) on this JPanel

				lastRenderTime = now;

				//Update the frames we got.
				int thisSecond = (int) (lastUpdateTime / 1000000000);

				if (thisSecond > lastSecondTime)
				{
					//System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
					fps = frameCount;
					frameCount = 0;
					lastSecondTime = thisSecond;
				}

				//Yield until it has been at least the target time between renders. This saves the CPU from hogging.
				while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS
					&& now - lastUpdateTime < TIME_BETWEEN_UPDATES)
				{
					//allow the threading system to play threads that are waiting to run.
					Thread.yield();
					//This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
					//You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
					//FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
					//On my OS(Windows 7 x64 intel i3) it does not allow for time to make enityt etc move thus all stands still
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
		fps = 0;//no more running set fps to 0
	}

	private void drawGame()
	{
		//Both revalidate and repaint are thread-safe — you need not invoke them from the event-dispatching thread. http://docs.oracle.com/javase/tutorial/uiswing/layout/howLayoutWorks.html
		repaint();
	}

	private void updateGame(long elapsedTime)
	{
		updateEntityMovements(elapsedTime);
		checkForCollisions();
	}

	private void checkForCollisions()
	{
		Entity entity1 = entities.get(0);
		Entity entity2 = entities.get(1);

		boolean entity1Fails = entity1.intersects(entity2) || entity1.intersects(entity1);
		boolean entity2Fails = entity2.intersects(entity1) || entity2.intersects(entity2);

		if (entity1Fails)
		{
			running.set(false);
			JOptionPane.showConfirmDialog(this, "GAME OVER!", "SNAKE ONE BUMPED ITS HEAD!", JOptionPane.ERROR_MESSAGE);
		}
		else if (entity2Fails)
		{
			running.set(false);
			JOptionPane.showConfirmDialog(this, "GAME OVER!", "SNAKE TWO BUMPED ITS HEAD!", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateEntityMovements(long elapsedTime)
	{
		for (Entity e : entities)
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
		for (Entity e : entities)
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
		g2d.drawString("FPS: " + fps, 5, 10);
	}

	private void drawBackground(Graphics2D g2d)
	{
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		//thanks to trashgod for lovely testing background :) http://stackoverflow.com/questions/3256269/jtextfields-on-top-of-active-drawing-on-jpanel-threading-problems/3256941#3256941
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < 128; i++)
		{
			g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));//random color
			g2d.drawLine(getWidth() / 2, getHeight() / 2, random.nextInt(getWidth()), random.nextInt(getHeight()));
		}
	}
}