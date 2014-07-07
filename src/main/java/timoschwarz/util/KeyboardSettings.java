package timoschwarz.util;

import java.awt.event.KeyEvent;

public class KeyboardSettings
{

	public static Direction getDirectionForKey(int keyCode)
	{
		Direction direction = null;
		switch (keyCode)
		{
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				direction = Direction.UP;
			break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				direction = Direction.DOWN;
			break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				direction = Direction.LEFT;
			break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				direction = Direction.RIGHT;
			break;
			default:
			break;
		}

		return direction;
	}
}
