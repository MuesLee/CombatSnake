package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import timoschwarz.snake.util.VideoUtils;

public class GameConfigurator
{
	private String namePlayerOne;
	private String namePlayerTwo;
	private int snakeGrowSize = 1;

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

		int width = (int) (VideoUtils.getScreenWidth() * PERCENTAGE_OF_SCREEN_SIZE);
		int height = (int) (VideoUtils.getScreenHeight() * PERCENTAGE_OF_SCREEN_SIZE);
		Dimension preferredSize = new Dimension(width, height);
		frame.setPreferredSize(preferredSize);

		Integer[] items = new Integer[5];
		items[0] = 1;
		items[1] = 2;
		items[2] = 3;
		items[3] = 4;
		items[4] = 5;
		final JComboBox<Integer> comboBoxGrowSize = new JComboBox<>(items);
		comboBoxGrowSize.setSelectedIndex(0);
		comboBoxGrowSize.setMaximumRowCount(5);
		comboBoxGrowSize.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				snakeGrowSize = (int) comboBoxGrowSize.getSelectedItem();
			}
		});

		final JLabel labelGrowSize = new JLabel("Snake Grow Size");
		labelGrowSize.setLabelFor(comboBoxGrowSize);

		JButton startLocalGame = createStartLocalGameButton();
		JButton startLanGame = createStartLanGameButton();
		final JTextField textFieldPlayerOne = createTextFieldPlayerOne();
		textFieldPlayerOne.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				super.focusLost(e);
				namePlayerOne = textFieldPlayerOne.getText();
			}

			@Override
			public void focusGained(FocusEvent e)
			{
				super.focusGained(e);
				textFieldPlayerOne.selectAll();
			}
		});
		final JTextField textFieldPlayerTwo = createTextFieldPlayerTwo();
		textFieldPlayerTwo.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				namePlayerTwo = textFieldPlayerTwo.getText();
			}

			@Override
			public void focusGained(FocusEvent e)
			{
				super.focusGained(e);
				textFieldPlayerTwo.selectAll();
			}
		});

		JPanel configPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		configPanel.add(labelGrowSize);
		configPanel.add(comboBoxGrowSize);

		JPanel textFieldPanel = new JPanel(new FlowLayout());
		textFieldPanel.add(textFieldPlayerOne);
		textFieldPanel.add(textFieldPlayerTwo);

		frame.add(textFieldPanel, BorderLayout.NORTH);
		frame.add(startLocalGame, BorderLayout.WEST);
		frame.add(startLanGame, BorderLayout.EAST);
		frame.add(configPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
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
				frame.setVisible(false);
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
		LANController mc = new LANController(namePlayerOne, namePlayerTwo);
	}

	public void startLocalGame(String namePlayerOne, String namePlayerTwo)
	{
		GameController.SNAKE_GROW_SIZE = snakeGrowSize - 1;
		GameController gc = new GameController(namePlayerOne, namePlayerTwo);
	}
}
