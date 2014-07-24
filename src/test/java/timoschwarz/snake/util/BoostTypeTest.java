package timoschwarz.snake.util;

import java.util.Random;

import org.junit.Test;

public class BoostTypeTest
{
	@Test
	public void testPercentages() throws Exception
	{
		Random random = new Random();

		for (int i = 0; i < 10; i++)
		{
			final int nextInt = random.nextInt(101);
			BoostType boostTypeByPercentage = BoostType.getBoostTypeByPercentage(nextInt);
			System.out.println(boostTypeByPercentage);
		}
	}
}
