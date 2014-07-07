package timoschwarz.snake.view;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import timoschwarz.snake.model.Snake;

class AdvancedSpritesEntity extends Entity
{

	public AdvancedSpritesEntity(ArrayList<BufferedImage> images, ArrayList<Long> timings, Snake snake)
	{
		super(images, timings, snake);
	}

	void setAnimation(ArrayList<BufferedImage> images, ArrayList<Long> timings)
	{
		reset();//reset variables of animator class
		setFrames(images, timings);//set new frames for animation
	}
}