package timoschwarz.snake.model;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import timoschwarz.snake.util.Direction;

public class SnakeTest
{
	private Snake snake;

	@Before
	public void initSnake()
	{
		snake = new Snake(4, 0, 0);
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
		snake.addTail(0, 0);
		int actualSize = snake.getSize();

		assertEquals(expectedSize, actualSize);
	}

	@Test
	public void snakeIsMadeOfFourSnakePieces() throws Exception
	{
		List<SnakePiece> pieces = snake.getSnakePieces();
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
		SnakePiece piece = snake.getSnakePieces().get(0);
		SnakePieceType actualType = piece.getType();
		SnakePieceType expectedType = SnakePieceType.HEAD;
		assertEquals(expectedType, actualType);
	}

	@Test
	public void lastPieceOfSnakeIsTail() throws Exception
	{
		int size = snake.getSize();
		SnakePiece piece = snake.getSnakePieces().get(size - 1);
		SnakePieceType actualType = piece.getType();
		SnakePieceType expectedType = SnakePieceType.TAIL;
		assertEquals(expectedType, actualType);
	}

	@Test
	public void snakeIsMadeOfBodyPartsEqualToSizeMinusTwo() throws Exception
	{
		List<SnakePiece> pieces = snake.getSnakePieces();

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

	@Test
	public void snakeMovesCorrectlyDown() throws Exception
	{
		LinkedList<SnakePiece> expectedPieces = new LinkedList<SnakePiece>();
		expectedPieces.add(new SnakePiece(4, 1, SnakePieceType.HEAD));
		expectedPieces.add(new SnakePiece(4, 0, SnakePieceType.BODY));
		expectedPieces.add(new SnakePiece(3, 0, SnakePieceType.BODY));
		expectedPieces.add(new SnakePiece(2, 0, SnakePieceType.TAIL));

		snake.setDirection(Direction.DOWN);
		snake.move();

		List<SnakePiece> actualPieces = snake.getSnakePieces();
		assertEquals(expectedPieces, actualPieces);

	}

	@Test
	public void snakeMovesCorrectlyDownVertical() throws Exception
	{
		LinkedList<SnakePiece> pieces = new LinkedList<SnakePiece>();
		pieces.add(new SnakePiece(1, 4, SnakePieceType.HEAD));
		pieces.add(new SnakePiece(1, 3, SnakePieceType.BODY));
		pieces.add(new SnakePiece(1, 2, SnakePieceType.BODY));
		pieces.add(new SnakePiece(1, 1, SnakePieceType.TAIL));

		snake.setSnakePieces(pieces);
		snake.setDirection(Direction.DOWN);
		snake.move();

		LinkedList<SnakePiece> expectedPieces = new LinkedList<SnakePiece>();
		expectedPieces.add(new SnakePiece(1, 5, SnakePieceType.HEAD));
		expectedPieces.add(new SnakePiece(1, 4, SnakePieceType.BODY));
		expectedPieces.add(new SnakePiece(1, 3, SnakePieceType.BODY));
		expectedPieces.add(new SnakePiece(1, 2, SnakePieceType.TAIL));

		List<SnakePiece> actualPieces = snake.getSnakePieces();
		assertEquals(expectedPieces, actualPieces);

	}

	@Test
	public void snakeMovesCorrectlyRight() throws Exception
	{
		LinkedList<SnakePiece> pieces = new LinkedList<SnakePiece>();
		pieces.add(new SnakePiece(4, 4, SnakePieceType.HEAD));
		pieces.add(new SnakePiece(3, 4, SnakePieceType.BODY));
		pieces.add(new SnakePiece(2, 4, SnakePieceType.BODY));
		pieces.add(new SnakePiece(1, 4, SnakePieceType.TAIL));

		snake.setSnakePieces(pieces);

		LinkedList<SnakePiece> expectedPieces = new LinkedList<SnakePiece>();
		expectedPieces.add(new SnakePiece(5, 4, SnakePieceType.HEAD));
		expectedPieces.add(new SnakePiece(4, 4, SnakePieceType.BODY));
		expectedPieces.add(new SnakePiece(3, 4, SnakePieceType.BODY));
		expectedPieces.add(new SnakePiece(2, 4, SnakePieceType.TAIL));

		snake.setDirection(Direction.RIGHT);
		snake.move();

		List<SnakePiece> actualPieces = snake.getSnakePieces();
		assertEquals(expectedPieces, actualPieces);

	}

	//	@Test
	//	public void snakeMovesCorrectlyLeft() throws Exception
	//	{
	//		LinkedList<SnakePiece> pieces = new LinkedList<SnakePiece>();
	//		pieces.add(new SnakePiece(4, 4, SnakePieceType.HEAD));
	//		pieces.add(new SnakePiece(3, 4, SnakePieceType.BODY));
	//		pieces.add(new SnakePiece(2, 4, SnakePieceType.BODY));
	//		pieces.add(new SnakePiece(1, 4, SnakePieceType.TAIL));
	//
	//		snake.setPieces(pieces);
	//
	//		LinkedList<SnakePiece> expectedPieces = new LinkedList<SnakePiece>();
	//		expectedPieces.add(new SnakePiece(3, 4, SnakePieceType.HEAD));
	//		expectedPieces.add(new SnakePiece(4, 4, SnakePieceType.BODY));
	//		expectedPieces.add(new SnakePiece(3, 4, SnakePieceType.BODY));
	//		expectedPieces.add(new SnakePiece(2, 4, SnakePieceType.TAIL));
	//
	//		snake.forceDirection(Direction.RIGHT);
	//		snake.move();
	//
	//		List<SnakePiece> actualPieces = snake.getPieces();
	//		assertEquals(expectedPieces, actualPieces);
	//
	//	}

	@Test
	public void snakeIsCorrectlyPlacedInitially() throws Exception
	{
		LinkedList<SnakePiece> expectedPieces = new LinkedList<SnakePiece>();
		expectedPieces.add(new SnakePiece(4, 0, SnakePieceType.HEAD));
		expectedPieces.add(new SnakePiece(3, 0, SnakePieceType.BODY));
		expectedPieces.add(new SnakePiece(2, 0, SnakePieceType.BODY));
		expectedPieces.add(new SnakePiece(1, 0, SnakePieceType.TAIL));

		List<SnakePiece> actualPieces = snake.getSnakePieces();
		assertEquals(expectedPieces, actualPieces);

	}

}
