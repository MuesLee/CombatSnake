package timoschwarz.snake.model;

import java.awt.Color;
import java.awt.Image;
import java.util.Timer;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.util.SnakeTask;

public class SpeedBoost extends Piece implements Boost {

	private int oldSnakeSpeed;

	public SpeedBoost(int x, int y) {
		super(x, y);
	}

	@Override
	public void modifySnake(final Snake snake) {
		final int speed = snake.getMovementSpeed();
		snake.setMovementSpeed(speed * 2);
		oldSnakeSpeed = speed;

		Timer timer = new Timer();
		timer.schedule(new SnakeTask(snake, this, timer),
				GameController.DURATION_SPEEDBOOSTER);
	}

	@Override
	public Color getColor() {
		return Color.YELLOW;
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public void restoreSnake(Snake snake) {
		snake.setMovementSpeed(oldSnakeSpeed);

	}

	@Override
	public String toString() {
		return "SPEEDBOOST";
	}

	@Override
	public String getSoundFileName() {
		return "boost_speed";
	}
}
