package timoschwarz.snake;

public class SnakePiece
{
	private SnakePieceType type;

	public SnakePiece(SnakePieceType type)
	{
		this.setType(type);
	}

	public SnakePieceType getType()
	{
		return type;
	}

	public void setType(SnakePieceType type)
	{
		this.type = type;
	}

}
