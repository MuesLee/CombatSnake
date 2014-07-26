package timoschwarz.snake.util;

import java.util.Timer;
import java.util.TimerTask;

import timoschwarz.snake.controller.GameController;

public class WorldChangerEventTask extends TimerTask {

	private GameController world;
	private Timer timer;

	public WorldChangerEventTask(GameController world, Timer timer) {
		this.world = world;
		this.timer = timer;
	}

	@Override
	public void run() {
		world.spawnNewWorldChanger();
		timer.cancel();
		cancel();
	}
}
