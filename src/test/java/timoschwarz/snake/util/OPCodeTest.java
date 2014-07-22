package timoschwarz.snake.util;

import org.junit.Test;

import timoschwarz.util.OPCode;

public class OPCodeTest
{
	@Test
	public void canConvertStringRIGHTtoOPCodeRIGHT() throws Exception
	{
		String line = "RIGHT";
		OPCode.convertStringToOPCode(line);
	}

}
