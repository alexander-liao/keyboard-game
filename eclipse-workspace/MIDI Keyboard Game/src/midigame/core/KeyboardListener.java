package midigame.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import midigame.ui.VirtualKeyboard;
import midigame.utils.Utils;

public class KeyboardListener implements MouseListener, MouseMotionListener, KeyListener {
	private final VirtualKeyboard keyboard;

	public KeyboardListener(VirtualKeyboard keyboard) {
		this.keyboard = keyboard;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_V) {
			this.keyboard.octaveDown();
		} else if (e.getKeyCode() == KeyEvent.VK_B) {
			this.keyboard.octaveUp();
		} else {
			this.keyboard.press(Utils.KEY_MAP.getOrDefault(e.getKeyCode(), -1));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.keyboard.release(Utils.KEY_MAP.getOrDefault(e.getKeyCode(), -1));
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
