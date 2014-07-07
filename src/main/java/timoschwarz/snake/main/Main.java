package timoschwarz.snake.main;

import timoschwarz.snake.controller.Controller;

public class Main {

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.showPlayground();
		controller.startSnakeOne();

	}

}
