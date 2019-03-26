package midigame.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import midigame.core.Game;
import midigame.core.KeyboardListener;
import midigame.utils.Utils;

@SuppressWarnings("serial")
public class VirtualKeyboardBackup extends JComponent {
	private final boolean[] pressed;
	private volatile int screenIn;
	private volatile int offset;
	
	private final List<Game> listeners;
	
	public VirtualKeyboardBackup() {
		this.pressed = new boolean[13];
		this.screenIn = -1;
		this.offset = 40;
		this.listeners = new ArrayList<>();
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		insertWhite(g, 0, false, true, 0);
		insertBlack(g, 0, 1);
		insertWhite(g, 45, true, true, 2);
		insertBlack(g, 45, 3);
		insertWhite(g, 90, true, false, 4);
		insertWhite(g, 135, false, true, 5);
		insertBlack(g, 135, 6);
		insertWhite(g, 180, true, true, 7);
		insertBlack(g, 180, 8);
		insertWhite(g, 225, true, true, 9);
		insertBlack(g, 225, 10);
		insertWhite(g, 270, true, false, 11);
		insertWhite(g, 315, false, false, 12);
	}
	
	private void insertWhite(Graphics g, int x, boolean left, boolean right, int pos) {
		if (pos + this.offset < 1) return;
		if (pos + this.offset == 1) left = false;
		g.setColor(this.pressed[pos] || pos == this.screenIn ? Color.GRAY : Color.WHITE);
		List<Integer> xs = new ArrayList<Integer>();
		List<Integer> ys = new ArrayList<Integer>();
		if (left) {
			xs.add(x + 1); ys.add(120);
			xs.add(x + 15); ys.add(120);
			xs.add(x + 15); ys.add(0);
		} else {
			xs.add(x + 1); ys.add(0);
		}
		
		if (right) {
			xs.add(x + 30); ys.add(0);
			xs.add(x + 30); ys.add(120);
			xs.add(x + 44); ys.add(120);
		} else {
			xs.add(x + 44); ys.add(0);
		}
		
		xs.add(x + 44); ys.add(200);
		xs.add(x + 1); ys.add(200);
		
		int len = xs.size();
		
		g.fillPolygon(Utils.primitize(xs.toArray(new Integer[len])), Utils.primitize(ys.toArray(new Integer[len])), len);
	}
	
	private void insertBlack(Graphics g, int x, int pos) {
		if (pos + this.offset <= 0) return;
		g.setColor(this.pressed[pos] || pos == this.screenIn ? Color.GRAY : Color.BLACK);
		g.fillRect(x + 30, 0, 30, 120);
	}
	
	public int getPos(int x, int y) {
		if (x < 0 || x >= 360 || y < 0 || y >= 200) return -1;
		if (y < 120) {
			for (int i = 30, j = 0; i <= 255; j += (i != 120 ? 1 : 0), i += 45) {
				if (i != 120 && i <= x && x <= i + 30) {
					return Utils.X_BLACK_MAP[j];
				}
			}
		}
		return Utils.X_WHITE_MAP[x / 45];
	}
	
	public void setScreenIn(int si) {
		this.sendSignal(false, this.screenIn);
		this.screenIn = si;
		this.sendSignal(true, this.screenIn);
		this.repaint();
	}
	
	public int getScreenIn() {
		return this.screenIn;
	}
	
	public void setPressed(int pos, boolean pressed) {
		if (pos >= 0 && pos <= 12) {
			this.pressed[pos] = pressed;
			this.repaint();
		}
	}
	
	public boolean getPressed(int pos) {
		return pos >= 0 && pos <= 12 && this.pressed[pos];
	}
	
	public void octaveUp() {
		if (this.offset < 76) {
			this.offset += 12;
			this.repaint();
		}
	}
	
	public void octaveDown() {
		if (this.offset > -8) {
			this.offset -= 12;
			this.repaint();
		}
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public void bind(Game game) {
		this.listeners.add(game);
	}
	
	public boolean unbind(Game game) {
		return this.listeners.remove(game);
	}
	
	private void sendSignal(boolean push, int key) {
		for (Game game : this.listeners) {
			if (push) {
				game.keyPress(key);
			} else {
				game.keyRelease(key);
			}
		}
	}
	
	public static void createKeyboard() {
		VirtualKeyboardBackup.createKeyboard(0, 0);
	}
	
	public static VirtualKeyboardBackup createKeyboard(int x, int y) {
		JFrame window = new JFrame("Virtual Keyboard");
		window.setSize(360, 200);
		window.setResizable(false);
		window.setLocation(x, y);
		
		VirtualKeyboardBackup keyboard = new VirtualKeyboardBackup();
		window.add(keyboard);
		
//		KeyboardListener listener = new KeyboardListener(keyboard, window);
//		
//		window.addMouseListener(listener);
//		window.addMouseMotionListener(listener);
//		window.addKeyListener(listener);

		window.setVisible(true);
		
		return keyboard;
	}
}
