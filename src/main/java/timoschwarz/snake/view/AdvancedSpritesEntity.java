package timoschwarz.snake.view;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import timoschwarz.snake.model.Snake;

class AdvancedSpritesEntity extends Entity
{

	public AdvancedSpritesEntity(ArrayList<BufferedImage> images, ArrayList<Long> timings, Snake snake)
	{
		super(images, timings, snake.getPieces());
	}

	void setAnimation(ArrayList<BufferedImage> images, ArrayList<Long> timings)
	{
		reset();
		setFrames(images, timings);
	}
}