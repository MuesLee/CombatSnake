package timoschwarz.snake.dao;

import java.util.List;

public interface HighscoreDAO {

	public List<Score> getHighscore();

	public boolean insertScore(Score score);

	public boolean isHighscore(int score);
}
