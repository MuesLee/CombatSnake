package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import timoschwarz.snake.model.Player;
import timoschwarz.util.VideoUtils;

public class LANLobby
{
	private static final String TITLE_GAME_CONFIGURATION = "LAN Lobby";
	private static final double PERCENTAGE_OF_SCREEN_SIZE = 0.4;
	private JFrame frame;
	private Player[] players;
	private JList<Player> playerJList;
	private JButton startGameButton;
	private LANController controller;

	public LANLobby(LANController controller)
	{
		this.controller = controller;
		players = new Player[2];
		playerJList = new JList<>(players);
		configureFrame();
	}

	public void addPlayer(Player player)
	{
		if (players[0] == null)
		{
			players[0] = player;
		}
		else if (players[1] == null)
		{
			players[1] = player;
			startGameButton.setEnabled(true);
		}

		playerJList.setListData(players);
	}

	private void configureFrame()
	{
		frame = new JFrame(TITLE_GAME_CONFIGURATION);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				super.windowClosed(e);
				System.exit(0);
			}
		});
		int width = (int) (VideoUtils.getScreenWidth() * PERCENTAGE_OF_SCREEN_SIZE);
		int height = (int) (VideoUtils.getScreenHeight() * PERCENTAGE_OF_SCREEN_SIZE);
		Dimension preferredSize = new Dimension(width, height);
		frame.setPreferredSize(preferredSize);

		startGameButton = new JButton("Start Game");
		startGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				controller.startLANGame();
			}
		});
		startGameButton.setEnabled(false);

		JPanel playerPanel = new JPanel(new BorderLayout());
		playerJList.setVisible(true);
		playerJList.setVisibleRowCount(4);
		JLabel listLabel = new JLabel("Lobby Members:");
		listLabel.setLabelFor(playerJList);
		playerPanel.add(listLabel, BorderLayout.NORTH);
		playerPanel.add(playerJList, BorderLayout.CENTER);

		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setStringPainted(true);
		bar.setString("Waiting for Player...");

		frame.add(bar, BorderLayout.CENTER);
		frame.add(playerPanel, BorderLayout.NORTH);
		frame.add(startGameButton, BorderLayout.SOUTH);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
