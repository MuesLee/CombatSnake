package timoschwarz.snake;

import java.util.LinkedList;
import java.util.List;

public class Snake
{
	private int size;
	private List<SnakePiece> pieces;

	public Snake(int size)
	{
		this.setSize(size);
		initSnake();
	}

	private void initSnake()
	{
		pieces = new LinkedList<SnakePiece>();

		for (int i = 0; i < size; i++)
		{
			if (i == 0)
			{
				getPieces().add(new SnakePiece(SnakePieceType.HEAD));
			}
			else if (i == size - 1)
			{
				getPieces().add(new SnakePiece(SnakePieceType.TAIL));
			}
			else
			{
				getPieces().add(new SnakePiece(SnakePieceType.BODY));
			}

		}

	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public void addTail()
	{
		this.size++;

	}

	public List<SnakePiece> getPieces()
	{
		return pieces;
	}

	public void setPieces(List<SnakePiece> pieces)
	{
		this.pieces = pieces;
	}

}
