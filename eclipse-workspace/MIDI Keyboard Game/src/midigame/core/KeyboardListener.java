package midigame.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import midigame.ui.VirtualKeyboard;
import midigame.utils.Utils;

public class KeyboardListener implements MouseListener, MouseMotionListener, KeyListener {
	private final VirtualKeyboard keyboard;
	
	private final Set<Integer> keys;
	
	private boolean shift = false, ctrl = false;
	
	private final Map<Integer, Integer> mem;

	public KeyboardListener(VirtualKeyboard keyboard) {
		this.keyboard = keyboard;
		this.keys = new HashSet<>();
		this.mem = new HashMap<>();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (this.keys.contains(e.getKeyCode())) return;
		if (e.getKeyCode() == KeyEvent.VK_V) {
			this.keyboard.octaveDown();
		} else if (e.getKeyCode() == KeyEvent.VK_B) {
			this.keyboard.octaveUp();
		} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			this.shift = this.keyboard.octaveUp();
		} else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			this.ctrl = this.keyboard.octaveDown();
		} else {
			int key = Utils.KEY_MAP.getOrDefault(e.getKeyCode(), -1);
			if (key != -1) {
				this.keyboard.press(key);
				this.mem.put(key, this.keyboard.getOffset() + key);
			}
		}
		this.keys.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.keys.remove(e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			if (this.shift) {
				this.keyboard.octaveDown();
				this.shift = false;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			if (this.ctrl) {
				this.keyboard.octaveUp();
				this.ctrl = false;
			}
		} else {
			int key = Utils.KEY_MAP.getOrDefault(e.getKeyCode(), -1);
			if (key != -1 && this.mem.containsKey(key)) {
				this.keyboard.release(this.mem.remove(key));
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.keyboard.setScreenIn(this.keyboard.getPos(e.getX(), e.getY()));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.keyboard.setScreenIn(-1);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.keyboard.setScreenIn(this.keyboard.getPos(e.getX(), e.getY()));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.keyboard.setScreenIn(-1);
	}

}
