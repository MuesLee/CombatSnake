package timoschwarz.snake;

public enum SnakePieceType {

	BODY("BODY"), HEAD("HEAD"), TAIL("TAIL");

	private String name;

	SnakePieceType(String name)
	{
		this.setName(name);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
