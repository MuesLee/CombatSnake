package timoschwarz.snake.model;

public class SnakePiece
{
	private SnakePieceType type;
	private int x;
	private int y;

	public SnakePiece(int x, int y, SnakePieceType type)
	{
		this.x = x;
		this.y = y;
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

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	@Override
	public String toString()
	{
		String text = "X = " + x + ", Y = " + y + ", Type = " + type;
		return text;
	}

	@Override
	public SnakePiece clone()
	{
		SnakePiece clone = new SnakePiece(this.x, this.y, this.type);

		return clone;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (type == null ? 0 : type.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		SnakePiece other = (SnakePiece) obj;
		if (type != other.type)
		{
			return false;
		}
		if (x != other.x)
		{
			return false;
		}
		if (y != other.y)
		{
			return false;
		}
		return true;
	}

}
