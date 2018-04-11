package Main;

import javax.swing.*;

public class GameApplication {

	private static JFrame window;
	public static void main(String[] args) 
	{
		window = new JFrame("Knight Game");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}

	/**
	 * метод для передачи объекта окна для работы с ним в других классах
	 * @return jpanel
	 */
	public static JFrame getWindow() {
		return window;
	}

}
