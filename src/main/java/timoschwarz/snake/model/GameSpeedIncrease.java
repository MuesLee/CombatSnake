package timoschwarz.snake.model;

import java.awt.Color;
import java.awt.Image;
import java.util.Timer;

import timoschwarz.snake.controller.GameController;
import timoschwarz.snake.util.WorldChangerTask;

public class GameSpeedIncrease extends Piece implements WorldChanger {

	public GameSpeedIncrease(int x, int y) {
		super(x, y);
	}

	@Override
	public void modifyWorld(World world) {
		int gameSpeed = GameController.GAME_SPEED;
		gameSpeed = (int) (gameSpeed / 2);
		GameController.GAME_SPEED = gameSpeed;

		Timer timer = new Timer();
		timer.schedule(new WorldChangerTask(this, timer, world),
				GameController.WORLD_CHANGER_SPEED_INCREASE_DURATION);
	}

	@Override
	public void restoreWorld(World world) {

		GameController.GAME_SPEED = GameController.DEFAULT_GAME_SPEED;

	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return Color.pink;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSoundFile() {
		// TODO Auto-generated method stub
		return "worldChanger_GameSpeedIncrease";
	}

}
