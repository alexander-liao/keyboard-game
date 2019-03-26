package midigame.utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class Utils {
	private Utils() { }
	
	public static final int[] X_WHITE_MAP = {0, 2, 4, 5, 7, 9, 11, 12};
	public static final int[] X_BLACK_MAP = {1, 3, 6, 8, 10};
	
	public static final Map<Integer, Integer> KEY_MAP = new HashMap<>();
	
	public static final Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
	
	static {
		KEY_MAP.put(KeyEvent.VK_A, 0);
		KEY_MAP.put(KeyEvent.VK_W, 1);
		KEY_MAP.put(KeyEvent.VK_S, 2);
		KEY_MAP.put(KeyEvent.VK_E, 3);
		KEY_MAP.put(KeyEvent.VK_D, 4);
		KEY_MAP.put(KeyEvent.VK_F, 5);
		KEY_MAP.put(KeyEvent.VK_T, 6);
		KEY_MAP.put(KeyEvent.VK_G, 7);
		KEY_MAP.put(KeyEvent.VK_Y, 8);
		KEY_MAP.put(KeyEvent.VK_H, 9);
		KEY_MAP.put(KeyEvent.VK_U, 10);
		KEY_MAP.put(KeyEvent.VK_J, 11);
		KEY_MAP.put(KeyEvent.VK_K, 12);
	}
	
	public static final int[] primitize(Integer[] array) {
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		return result;
	}
	
	public static final void playMIDI(Receiver recv, int note, int vol) {
		ShortMessage sm = new ShortMessage();
		try {
			sm.setMessage(ShortMessage.NOTE_ON, 0, note, vol);
		} catch (InvalidMidiDataException e) {
			System.err.println("Alex you dumb");
		}
		recv.send(sm, -1);
	}
	
	public static final void stopMIDI(Receiver recv, int note) {
		ShortMessage sm = new ShortMessage();
		try {
			sm.setMessage(ShortMessage.NOTE_OFF, 0, note, 0);
		} catch (InvalidMidiDataException e) {
			System.err.println("Alex you dumb");
		}
		recv.send(sm, -1);
	}
}