package timoschwarz.snake.util;

import org.junit.Assert;
import org.junit.Test;

public class OPCodeTest
{
	@Test
	public void canConvertStringRIGHTtoOPCodeRIGHT() throws Exception
	{
		String input = "RIGHT";
		OPCode actual = OPCode.convertStringToOPCode(input);
		Assert.assertEquals(OPCode.RIGHT, actual);
	}

	@Test
	public void canConvertStringLEFTtoOPCodeLEFT() throws Exception
	{
		String input = "LEFT";
		OPCode actual = OPCode.convertStringToOPCode(input);
		Assert.assertEquals(OPCode.LEFT, actual);
	}

	@Test
	public void canConvertStringUPtoOPCodeUP() throws Exception
	{
		String input = "UP";
		OPCode actual = OPCode.convertStringToOPCode(input);
		Assert.assertEquals(OPCode.UP, actual);
	}

	@Test
	public void canConvertStringDOWNtoOPCodeDOWN() throws Exception
	{
		String input = "DOWN";
		OPCode actual = OPCode.convertStringToOPCode(input);
		Assert.assertEquals(OPCode.DOWN, actual);
	}

}
