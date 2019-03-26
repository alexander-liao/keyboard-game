package midigame.ui;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import midigame.core.Game;
import midigame.utils.Utils;

@SuppressWarnings("serial")
public class Main extends JComponent {
	public static final boolean DEBUG = true;

	public Main() {
		
	}

	public static void main(String[] args) {
		JFrame window = new JFrame("MIDIGame");
		window.setSize(800, 500);
		window.setLocation(Utils.SCREEN.width / 2 - window.getWidth() / 2, Utils.SCREEN.height / 2 - window.getHeight() / 2);
		window.setResizable(false);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		Main main = new Main();
		Game game = new Game();
		window.add(main);
		JTextArea instructions = new JTextArea("The keyboard is as follows:\n"
				+ " W E   T Y U\n"
				+ "A S D F G H J K\n"
				+ "Press V to move the selected region down and B to move it up.\n"
				+ "You can also click on the keys.\n"
				+ "If the keyboard has weird grey area around it, restart the program.");
		instructions.setEditable(false);
		instructions.setMargin(new Insets(5, 5, 5, 5));
		instructions.setFont(new Font("Consolas", 0, 16));
		window.add(instructions);
		window.setVisible(true);
		
		if (DEBUG) {
			window.setLocation(Utils.SCREEN.width / 2 - window.getWidth() / 2, 0);
			VirtualKeyboard keyboard = VirtualKeyboard.createKeyboard();
			keyboard.bind(game);
		}
	}
}