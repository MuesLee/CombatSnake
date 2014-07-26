package timoschwarz.snake.util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class KeyBindings {

	private Direction directionForPlayerOne = Direction.RIGHT;
	private Direction directionForPlayerTwo = Direction.RIGHT;

	public KeyBindings(JComponent gp) {
		// SNAKE TWO

		gp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "W pressed");
		gp.getActionMap().put("W pressed", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				setDirectionForPlayerTwo(Direction.UP);
			}
		});

		gp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "S pressed");
		gp.getActionMap().put("S pressed", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				setDirectionForPlayerTwo(Direction.DOWN);
			}
		});
		gp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "A pressed");
		gp.getActionMap().put("A pressed", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				setDirectionForPlayerTwo(Direction.LEFT);
			}
		});
		gp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "D pressed");
		gp.getActionMap().put("D pressed", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				setDirectionForPlayerTwo(Direction.RIGHT);
			}
		});

		// SNAKE ONE

		gp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false),
				"down pressed");
		gp.getActionMap().put("down pressed", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				setDirectionForPlayerOne(Direction.DOWN);
			}
		});
		gp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "up pressed");
		gp.getActionMap().put("up pressed", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				setDirectionForPlayerOne(Direction.UP);
			}
		});
		gp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false),
				"left pressed");
		gp.getActionMap().put("left pressed", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				setDirectionForPlayerOne(Direction.LEFT);
			}
		});
		gp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false),
				"right pressed");
		gp.getActionMap().put("right pressed", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				setDirectionForPlayerOne(Direction.RIGHT);
			}
		});
	}

	public synchronized Direction getDirectionForPlayerOne() {
		return directionForPlayerOne;
	}

	public synchronized void setDirectionForPlayerOne(
			Direction directionForPlayerOne) {
		this.directionForPlayerOne = directionForPlayerOne;
	}

	public synchronized Direction getDirectionForPlayerTwo() {
		return directionForPlayerTwo;
	}

	public synchronized void setDirectionForPlayerTwo(
			Direction directionForPlayerTwo) {
		this.directionForPlayerTwo = directionForPlayerTwo;
	}
}