package timoschwarz.snake.util;

import java.util.Timer;
import java.util.TimerTask;

import timoschwarz.snake.model.World;
import timoschwarz.snake.model.powerups.WorldChanger;

public class WorldChangerTask extends TimerTask {

	private WorldChanger worldChanger;
	private Timer timer;
	private World world;

	public WorldChangerTask(WorldChanger worldChanger, Timer timer, World world) {
		super();
		this.worldChanger = worldChanger;
		this.timer = timer;
		this.world = world;
	}

	@Override
	public void run() {
		worldChanger.restoreWorld(world);
		timer.cancel();
		cancel();
	}

}
