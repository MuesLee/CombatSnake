package timoschwarz.snake.dao;

public class Score implements Comparable<Score> {

	private String name;
	private int score;

	public Score(String name, int score) {
		this.setName(name);
		this.setScore(score);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public int compareTo(Score o) {

		return new Integer(getScore()).compareTo(new Integer(o.getScore()));
	}

}
