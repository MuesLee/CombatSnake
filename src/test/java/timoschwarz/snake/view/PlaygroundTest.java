package timoschwarz.snake.view;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.junit.Test;

import timoschwarz.snake.view.Playground;

public class PlaygroundTest
{

	@Test
	public void canCreatePlaygroundWithSizeOf100to100() throws Exception
	{
		Dimension expectedSize = new Dimension(100, 100);
		Playground playground = new Playground(expectedSize, null);
		Dimension actualSize = playground.getSize();
		assertEquals(expectedSize, actualSize);
	}

}
