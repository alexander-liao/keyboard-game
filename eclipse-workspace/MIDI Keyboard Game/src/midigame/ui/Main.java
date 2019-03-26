package midigame.ui;

import java.awt.Font;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
		Synthesizer synth;
		Receiver recv;
		try {
			synth = MidiSystem.getSynthesizer();

			JComboBox<String> options = new JComboBox<String>();
			Map<String, Instrument> instruments = new HashMap<>();

			for (Instrument inst : synth.getAvailableInstruments()) {
				options.addItem(inst.getName());
				instruments.put(inst.getName(), inst);
			}
			
			JOptionPane.showConfirmDialog(null, options, "Select an instrument", JOptionPane.PLAIN_MESSAGE);

			Instrument instrument = instruments.get(options.getSelectedItem());
			synth.loadInstrument(instrument);
			
			ShortMessage sm = new ShortMessage();
			sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument.getPatch().getProgram(), 0);
			
			recv = MidiSystem.getReceiver();
			recv.send(sm, -1);
		} catch (MidiUnavailableException | InvalidMidiDataException e) {
			System.err.println("Unfortunately, MIDI appears to not work on this device.");
			return;
		}
		
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
				+ "Press V to move the selected region down an octave and B to move it up.\n"
				+ "Shift and Control temporarily shift the region up/down respectively, reversing on release.\n"
				+ "You can also click on the keys.\n"
				+ "If the keyboard has weird grey area around it, restart the program.");
		instructions.setEditable(false);
		instructions.setMargin(new Insets(5, 5, 5, 5));
		instructions.setFont(new Font("Consolas", 0, 16));
		window.add(instructions);
		window.setVisible(true);
		
		if (DEBUG) {
			window.setLocation(Utils.SCREEN.width / 2 - window.getWidth() / 2, 0);
			VirtualKeyboard keyboard = VirtualKeyboard.createKeyboard(recv, synth);
			keyboard.bind(game);
		}
	}
}