package timoschwarz.snake.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighscoreFileDAO implements HighscoreDAO {

	private static final Path db = Paths.get("./data/highscore.db");
	private List<Score> scores;

	public HighscoreFileDAO() {
	}

	@Override
	public List<Score> getBestenliste() {

		if (scores != null) {
			return scores;
		}

		List<Score> ret = new ArrayList<Score>(10);

		if (!Files.exists(db)) {
			scores = ret;
			return ret;
		}

		try {
			readIntoList(ret);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(ret, Collections.reverseOrder());

		scores = ret;
		return ret;
	}

	private void readIntoList(List<Score> ret) throws IOException {

		if (!Files.exists(db)) {
			return;
		}

		BufferedReader in = Files.newBufferedReader(db,
				Charset.forName("UTF-8"));

		String line = null;

		while ((line = in.readLine()) != null) {

			try {
				String name = line;

				String score = (line = in.readLine());
				int scoreI = Integer.valueOf(score);

				ret.add(new Score(name, scoreI));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		in.close();
	}

	private void writeFromList(List<Score> list) throws IOException {

		if (!Files.exists(db)) {

			Path dir = db.getParent();
			if (!Files.exists(dir)) {
				Files.createDirectories(dir);
			}

			Files.createFile(db);
		}

		scores = list;

		BufferedWriter out = Files.newBufferedWriter(db,
				Charset.forName("UTF-8"));

		for (Score s : list) {

			out.write(s.getName());
			out.newLine();

			out.write(s.getScore() + "");
			out.newLine();
		}

		out.close();
	}

	@Override
	public boolean insertScore(Score score) {

		List<Score> list = getBestenliste();

		if (list.size() >= 10 && list.get(9).getScore() >= score.getScore()) {
			return false;
		}

		list.add(score);
		Collections.sort(list, Collections.reverseOrder());

		list = list.subList(0, Math.min(list.size(), 10));

		try {
			writeFromList(list);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean isHighscore(int score) {
		List<Score> list = getBestenliste();

		if (list.size() < 10) {
			return true;
		}

		if (list.get(9).getScore() < score) {
			return true;
		}

		return false;
	}

}
