package midigame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import midigame.core.Game;
import midigame.core.KeyboardListener;
import midigame.utils.Constants;
import midigame.utils.Utils;

@SuppressWarnings("serial")
public class VirtualKeyboard extends JComponent {
	private final boolean[] pressed;
	private volatile int screenIn;
	private volatile int offset;
	
	public final JFrame window;
	
	private final List<Game> listeners;
	
	public VirtualKeyboard(JFrame window) {
		this.pressed = new boolean[88];
		this.screenIn = -1;
		this.offset = 39;
		this.listeners = new ArrayList<>();
		this.window = window;
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int w = Constants.WHITE_KEY_WIDTH;
		
		insertWhite(g, 0, false, true, 0);
		insertBlack(g, 0, 1);
		insertWhite(g, w, true, false, 2);
		
		int x = w;
		int pos = 3;
		
		for (int i = 0; i < 7; i++) {
			insertWhite(g, x += w, false, true, pos++);
			insertBlack(g, x, pos++);
			insertWhite(g, x += w, true, true, pos++);
			insertBlack(g, x, pos++);
			insertWhite(g, x += w, true, false, pos++);
			insertWhite(g, x += w, false, true, pos++);
			insertBlack(g, x, pos++);
			insertWhite(g, x += w, true, true, pos++);
			insertBlack(g, x, pos++);
			insertWhite(g, x += w, true, true, pos++);
			insertBlack(g, x, pos++);
			insertWhite(g, x += w, true, false, pos++);
		}
		
		insertWhite(g, x += w, false, false, pos++);
	}
	
	private void insertWhite(Graphics g, int x, boolean left, boolean right, int pos) {
		g.setColor(this.screenIn == pos || this.getPressed(pos) ? Color.GRAY : Color.WHITE);
		List<Integer> xs = new ArrayList<Integer>();
		List<Integer> ys = new ArrayList<Integer>();
		if (left) {
			xs.add(x + 1); ys.add(Constants.BLACK_KEY_HEIGHT);
			xs.add(x + Constants.BLACK_KEY_SEMIWIDTH); ys.add(Constants.BLACK_KEY_HEIGHT);
			xs.add(x + Constants.BLACK_KEY_SEMIWIDTH); ys.add(0);
		} else {
			xs.add(x + 1); ys.add(0);
		}
		
		if (right) {
			xs.add(x + Constants.WHITE_KEY_WIDTH - Constants.BLACK_KEY_SEMIWIDTH); ys.add(0);
			xs.add(x + Constants.WHITE_KEY_WIDTH - Constants.BLACK_KEY_SEMIWIDTH); ys.add(Constants.BLACK_KEY_HEIGHT);
			xs.add(x + Constants.WHITE_KEY_WIDTH - 1); ys.add(Constants.BLACK_KEY_HEIGHT);
		} else {
			xs.add(x + Constants.WHITE_KEY_WIDTH); ys.add(0);
		}
		
		xs.add(x + Constants.WHITE_KEY_WIDTH - 1); ys.add(Constants.WHITE_KEY_HEIGHT);
		xs.add(x + 1); ys.add(Constants.WHITE_KEY_HEIGHT);
		
		int len = xs.size();
		
		g.fillPolygon(Utils.primitize(xs.toArray(new Integer[len])), Utils.primitize(ys.toArray(new Integer[len])), len);
		if (pos >= this.offset && pos <= this.offset + 13) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x, Constants.WHITE_KEY_HEIGHT - 10, Constants.WHITE_KEY_WIDTH, 10);
		}
	}
	
	private void insertBlack(Graphics g, int x, int pos) {
		g.setColor(this.screenIn == pos || this.getPressed(pos) ? Color.GRAY : Color.BLACK);
		g.fillRect(x + Constants.WHITE_KEY_WIDTH - Constants.BLACK_KEY_SEMIWIDTH, 0, Constants.BLACK_KEY_SEMIWIDTH * 2, Constants.BLACK_KEY_HEIGHT);
	}
	
	public int getPos(int x, int y) {
		if (x < 0 || x >= this.getWidth() || y < 0 || y >= this.getHeight()) return -1;
		if (x > Constants.WHITE_KEY_WIDTH - Constants.BLACK_KEY_SEMIWIDTH
		 && x < Constants.WHITE_KEY_WIDTH + Constants.BLACK_KEY_SEMIWIDTH
		 && y < Constants.BLACK_KEY_HEIGHT) {
			return 1;
		} else if (x < Constants.WHITE_KEY_WIDTH) {
			return 0;
		} else if (x < Constants.WHITE_KEY_WIDTH * 2) {
			return 2;
		} else {
			x -= Constants.WHITE_KEY_WIDTH * 2;
			int octave = x / (Constants.WHITE_KEY_WIDTH * 7);
			x %= Constants.WHITE_KEY_WIDTH * 7;
			if (y < Constants.BLACK_KEY_HEIGHT) {
				for (int i = Constants.WHITE_KEY_WIDTH - Constants.BLACK_KEY_SEMIWIDTH, j = 0;
					 i <= Constants.WHITE_KEY_WIDTH * 6 - Constants.BLACK_KEY_SEMIWIDTH;
					 i += Constants.WHITE_KEY_WIDTH) {
					if (i == Constants.WHITE_KEY_WIDTH * 3 - Constants.BLACK_KEY_SEMIWIDTH) continue;
					if (i <= x && x <= i + Constants.BLACK_KEY_SEMIWIDTH * 2) {
						return Utils.X_BLACK_MAP[j] + octave * 12 + 3;
					}
					j++;
				}
			}
			return Utils.X_WHITE_MAP[x / Constants.WHITE_KEY_WIDTH] + octave * 12 + 3;
		}
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
	
	public void press(int pos) {
		if (pos == -1) return;
		pos += this.offset;
		if (pos >= 0 && pos < 88) {
			this.pressed[pos] = true;
		}
		this.repaint();
	}
	
	public void release(int pos) {
		if (pos == -1) return;
		for (int i = (pos + 3) % 12; i < 88; i += 12) {
			this.pressed[i] = false;
		}
		this.repaint();
	}
	
	public boolean getPressed(int pos) {
		return this.pressed[pos];
	}
	
	public void octaveUp() {
		if (this.offset < 75) {
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
	
	public static VirtualKeyboard createKeyboard() {
		JFrame window = new JFrame();
//		window.setSize(52 * Constants.WHITE_KEY_WIDTH, Constants.WHITE_KEY_HEIGHT);
		window.setResizable(false);
		window.setLocation(Utils.SCREEN.width / 2 - (52 * Constants.WHITE_KEY_WIDTH) / 2, Utils.SCREEN.height - Constants.WHITE_KEY_HEIGHT - 75);
		
		VirtualKeyboard keyboard = new VirtualKeyboard(window);
		window.add(keyboard);
		
		int w = 52 * Constants.WHITE_KEY_WIDTH;
		int h = Constants.WHITE_KEY_HEIGHT;
		
		Dimension d = new Dimension(w, h);
		
		keyboard.setSize(d);
		keyboard.setPreferredSize(d);
		keyboard.setBounds(0, 0, keyboard.getWidth(), keyboard.getHeight());
		
		KeyboardListener listener = new KeyboardListener(keyboard);
		
		keyboard.addMouseListener(listener);
		keyboard.addMouseMotionListener(listener);
		window.addKeyListener(listener);

		window.pack();
		window.setVisible(true);
		
		return keyboard;
	}
}
