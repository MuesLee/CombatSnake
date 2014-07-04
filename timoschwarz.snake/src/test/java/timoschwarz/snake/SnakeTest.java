package timoschwarz.snake;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SnakeTest
{
	private Snake snake;

	@Before
	public void initSnake()
	{
		snake = new Snake(4);
	}

	@Test
	public void canCreateSnakeWithSizeOfFour() throws Exception
	{
		int expectedSize = 4;
		int actualSize = snake.getSize();

		assertEquals(expectedSize, actualSize);
	}

	@Test
	public void canIncreaseSnakeSizeByOne() throws Exception
	{
		int expectedSize = snake.getSize() + 1;
		snake.addTail();
		int actualSize = snake.getSize();

		assertEquals(expectedSize, actualSize);
	}

	@Test
	public void snakeIsMadeOfFourSnakePieces() throws Exception
	{
		List<SnakePiece> pieces = snake.getPieces();
		boolean snakeIsMadeOfSnakePieces = true;
		for (SnakePiece snakePiece : pieces)
		{
			if (snakePiece.getClass() != SnakePiece.class)
			{
				snakeIsMadeOfSnakePieces = false;
			}
		}
		assertTrue(snakeIsMadeOfSnakePieces);
	}

	@Test
	public void firstPieceOfSnakeIsHead() throws Exception
	{
		SnakePiece piece = snake.getPieces().get(0);
		SnakePieceType actualType = piece.getType();
		SnakePieceType expectedType = SnakePieceType.HEAD;
		assertEquals(expectedType, actualType);
	}

	@Test
	public void lastPieceOfSnakeIsTail() throws Exception
	{
		int size = snake.getSize();
		SnakePiece piece = snake.getPieces().get(size - 1);
		SnakePieceType actualType = piece.getType();
		SnakePieceType expectedType = SnakePieceType.TAIL;
		assertEquals(expectedType, actualType);
	}

	@Test
	public void snakeIsMadeOfBodyPartsEqualToSizeMinusTwo() throws Exception
	{
		List<SnakePiece> pieces = snake.getPieces();

		int actualBodyCount = 0;
		int expectedBodyCount = pieces.size() - 2;

		for (SnakePiece snakePiece : pieces)
		{
			if (snakePiece.getType() == SnakePieceType.BODY)
			{
				actualBodyCount++;
			}
		}
		assertEquals(expectedBodyCount, actualBodyCount);

	}
}
