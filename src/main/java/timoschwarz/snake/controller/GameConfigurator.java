package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import timoschwarz.util.VideoUtils;

public class GameConfigurator
{
	private String namePlayerOne;
	private String namePlayerTwo;

	private static final String TITLE_GAME_CONFIGURATION = "Combat Snakez Konfigurator";
	private static final String TEXT_BUTTON_START_LOCAL_GAME = "Start Local Game";
	private static final String TEXT_BUTTON_START_LAN_GAME = "Start LAN Game";
	private static final double PERCENTAGE_OF_SCREEN_SIZE = 0.2;
	JFrame frame;

	public GameConfigurator()
	{
		configureFrame();
		showFrame();
	}

	private void showFrame()
	{
		frame.setVisible(true);
	}

	private JTextField createTextFieldPlayerOne()
	{
		final JTextField namePlayer = new JTextField("Player One");
		namePlayer.setBackground(Color.WHITE);
		return namePlayer;
	}

	private JTextField createTextFieldPlayerTwo()
	{
		final JTextField namePlayer = new JTextField("Player Two");
		namePlayer.setBackground(Color.GRAY);
		return namePlayer;
	}

	private void configureFrame()
	{
		frame = new JFrame(TITLE_GAME_CONFIGURATION);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		int width = (int) (VideoUtils.getScreenWidth() * PERCENTAGE_OF_SCREEN_SIZE);
		int height = (int) (VideoUtils.getScreenHeight() * PERCENTAGE_OF_SCREEN_SIZE);
		Dimension preferredSize = new Dimension(width, height);
		frame.setPreferredSize(preferredSize);

		JButton startLocalGame = createStartLocalGameButton();
		JButton startLanGame = createStartLanGameButton();
		JTextField textFieldPlayerOne = createTextFieldPlayerOne();
		JTextField textFieldPlayerTwo = createTextFieldPlayerTwo();

		JPanel textFieldPanel = new JPanel(new FlowLayout());
		textFieldPanel.add(textFieldPlayerOne);
		textFieldPanel.add(textFieldPlayerTwo);

		frame.add(textFieldPanel, BorderLayout.NORTH);
		frame.add(startLocalGame, BorderLayout.WEST);
		frame.add(startLanGame, BorderLayout.EAST);
		frame.pack();
	}

	private JButton createStartLanGameButton()
	{
		JButton lanGame = new JButton(TEXT_BUTTON_START_LAN_GAME);
		lanGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				startLanGame(namePlayerOne, namePlayerTwo);
			}

		});

		return lanGame;
	}

	private JButton createStartLocalGameButton()
	{
		JButton startLocalGame = new JButton(TEXT_BUTTON_START_LOCAL_GAME);
		startLocalGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				startLocalGame(namePlayerOne, namePlayerTwo);
				frame.setVisible(false);
			}
		});

		return startLocalGame;
	}

	private void startLanGame(String namePlayerOne, String namePlayerTwo)
	{
		MultiplayerController mc = new MultiplayerController(namePlayerOne, namePlayerTwo);
	}

	public void startLocalGame(String namePlayerOne, String namePlayerTwo)
	{
		GameController gc = new GameController(namePlayerOne, namePlayerTwo);
	}
}
