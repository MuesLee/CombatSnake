package timoschwarz.snake;

import static org.junit.Assert.*;

import org.junit.Test;

public class SnakePieceTest
{
	@Test
	public void pieceCanStoreItsOwnCoordinates() throws Exception
	{
		int expectedX = 0;
		int expectedY = 0;
		SnakePiece piece = new SnakePiece(expectedX, expectedY, SnakePieceType.HEAD);
		int actualX = piece.getX();
		int actualY = piece.getY();
		assertEquals(expectedY, actualY);
		assertEquals(expectedX, actualX);
	}
}
