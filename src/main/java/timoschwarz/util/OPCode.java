package timoschwarz.util;


public enum OPCode {

	RIGHT(Direction.RIGHT), LEFT(Direction.LEFT), UP(Direction.UP), DOWN(Direction.DOWN), JOINED(null), DISCONNECTED(
		null);

	private Direction dir;

	private OPCode(Direction dir)
	{
		this.dir = dir;
	}

	public Direction getDirection()
	{
		return dir;
	}

	public static OPCode convertStringToOPCode(String line)
	{
		OPCode code = null;

		switch (line)
		{
			case "RIGHT":
				code = OPCode.RIGHT;
			break;
			case "LEFT":
				code = OPCode.LEFT;
			break;
			case "UP":
				code = OPCode.UP;
			break;
			case "DOWN":
				code = OPCode.DOWN;
			break;
			case "JOINED":
				code = OPCode.JOINED;
			break;
			case "DISCONNECTED":
				code = OPCode.DISCONNECTED;
			break;
		}

		return code;
	}

}
