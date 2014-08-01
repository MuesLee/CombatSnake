package timoschwarz.snake.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HighscoreSqlDAO implements HighscoreDAO
{

	private static final String server_url = "https://do-rh-studizubi1.doms.kvwl.de/CombatSnakez/Bestenliste";
	private static final String PWD_INSERT = "insert_score";
	private static final String PWD_GET = "get_score";
	private static final String PWD_CHECK = "check_score";

	static
	{
		try
		{
			trustSSL();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static InputStream sendPostRequest(String body) throws IOException
	{

		HttpsURLConnection connection = (HttpsURLConnection) new URL(server_url).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", String.valueOf(body.length()));

		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(body);
		writer.flush();

		writer.close();

		return connection.getInputStream();
	}

	private static boolean submitScore(Score sc) throws MalformedURLException, IOException
	{
		String answer = "";

		String body = getPOSTString(sc) + "&passwd=" + PWD_INSERT;

		BufferedReader reader = new BufferedReader(new InputStreamReader(sendPostRequest(body)));

		for (String line; (line = reader.readLine()) != null;)
		{
			answer += line + "\n";
		}

		reader.close();

		if (answer.startsWith("OK"))
		{
			return true;
		}

		return false;
	}

	private static String getPOSTString(Score sc) throws UnsupportedEncodingException
	{
		String ret;

		ret = String.format("name=%s&score=%s", encode(sc.getName()), encode(sc.getScore() + ""));

		return ret;
	}

	private static String encode(String string) throws UnsupportedEncodingException
	{
		return URLEncoder.encode(string, "UTF-8");
	}

	private static List<Score> fetchScores(int max_scores) throws IOException
	{
		List<Score> ret = new LinkedList<Score>();

		String body = getPOSTString(new Score("", max_scores)) + "&passwd=" + PWD_GET;

		BufferedReader reader = new BufferedReader(new InputStreamReader(sendPostRequest(body)));

		String status = reader.readLine();
		if (status == null || !status.equals("OK"))
		{
			return ret;
		}

		for (String line; (line = reader.readLine()) != null;)
		{
			ret.add(parse(line));
		}

		reader.close();

		return ret;
	}

	private boolean chkIsScore(int score) throws IOException
	{

		boolean ret = false;

		String body = getPOSTString(new Score("", score)) + "&passwd=" + PWD_CHECK;

		BufferedReader reader = new BufferedReader(new InputStreamReader(sendPostRequest(body)));

		String status = reader.readLine().trim();

		if (status != null && status.equals("OK"))
		{
			ret = true;
		}

		reader.close();

		return ret;
	}

	private static Score parse(String s)
	{
		String name = "";
		int score = 0;

		try
		{
			String[] split = s.trim().split(":");

			if (split.length >= 2)
			{
				name = split[0];
				score = Integer.valueOf(split[1]);
			}
		}
		catch (Exception e)
		{
		}

		return new Score(name, score);
	}

	private static void trustSSL() throws NoSuchAlgorithmException, KeyManagementException
	{
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
		{
			@Override
			public X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType)
			{
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType)
			{
			}
		} };

		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	@Override
	public List<Score> getHighscore()
	{

		List<Score> ret;
		try
		{
			ret = fetchScores(10);
		}
		catch (IOException e)
		{
			ret = new ArrayList<Score>(0);
			e.printStackTrace();
		}

		return ret;
	}

	@Override
	public boolean insertScore(Score score)
	{

		try
		{
			return submitScore(score);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isHighscore(int score)
	{

		try
		{
			return chkIsScore(score);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
