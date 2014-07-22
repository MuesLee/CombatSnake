package timoschwarz.snake.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import timoschwarz.util.VideoUtils;

public class LANConfigurator
{

	private static final String TITLE_GAME_CONFIGURATION = "LAN Configurator";
	private static final double PERCENTAGE_OF_SCREEN_SIZE = 0.2;
	private JFrame frame;
	JTextField textFieldOwnIP;
	JTextField textFieldPlayer;
	JTextField textFieldHostIP;
	JButton hostGameButton;
	JButton joinGameButton;

	LANController controller;

	public LANConfigurator(LANController controller)
	{
		this.controller = controller;
		configureFrame();
		frame.setVisible(true);
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

		hostGameButton = new JButton("Host Game");
		hostGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String playerName = textFieldPlayer.getText();
				controller.hostGame(playerName);
			}
		});
		joinGameButton = new JButton("Join Game");
		joinGameButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				final String hostIP = textFieldHostIP.getText();
				String playerName = textFieldPlayer.getText();
				if (!hostIP.isEmpty())
				{
					controller.joinGame(hostIP, playerName);
				}

			}
		});

		textFieldPlayer = createTextField("Your name here");
		textFieldHostIP = createTextField("");
		String ownIP = getOwnIP();
		textFieldOwnIP = createTextField(ownIP);
		textFieldOwnIP.setEditable(false);

		JLabel labelPlayerName = new JLabel("Player Name");
		labelPlayerName.setLabelFor(textFieldPlayer);

		JLabel labelHostIP = new JLabel("Host IP");
		labelPlayerName.setLabelFor(textFieldHostIP);

		JLabel labelOwnIP = new JLabel("Your IP");
		labelPlayerName.setLabelFor(textFieldOwnIP);

		JPanel textFieldPanel = new JPanel();
		final BoxLayout boxLayout = new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS);
		textFieldPanel.setLayout(boxLayout);
		textFieldPanel.add(labelPlayerName);
		textFieldPanel.add(textFieldPlayer);
		textFieldPanel.add(labelHostIP);
		textFieldPanel.add(textFieldHostIP);
		textFieldPanel.add(labelOwnIP);
		textFieldPanel.add(textFieldOwnIP);

		frame.add(textFieldPanel, BorderLayout.CENTER);
		frame.add(hostGameButton, BorderLayout.EAST);
		frame.add(joinGameButton, BorderLayout.WEST);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	private String getOwnIP()
	{
		try
		{
			return InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOT AVAILABLE :(";
	}

	private JTextField createTextField(String placeholder)
	{
		final JTextField namePlayer = new JTextField(placeholder);
		namePlayer.setBackground(Color.WHITE);
		return namePlayer;
	}

}
