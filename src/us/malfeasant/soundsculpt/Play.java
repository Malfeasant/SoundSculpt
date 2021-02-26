package us.malfeasant.soundsculpt;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Play {
	private static final int SAMPLE_RATE = 48000;
	private static final int BITS_PER_SAMPLE = 16;
	private static AudioFormat FORMAT =
			new AudioFormat(SAMPLE_RATE, BITS_PER_SAMPLE, 1, true, false);	// 48kHz signed 16-bit mono should be doable anywhere...
	
	private volatile boolean playing;	// volatile so it can be set by gui thread, read by playback thread
	private final CopyOnWriteArrayList<Double> amps;
	
	public Play(ToneControl[] tca) {
		amps = new CopyOnWriteArrayList<>();
		try {
			SourceDataLine line = AudioSystem.getSourceDataLine(FORMAT);
			new Thread(() -> start(line), "Audio Playback").start();
			for (int i = 0; i < tca.length; i++) {
				amps.add(0.0);	// initial value- needs to be add()ed or else later set() doesn't work
				int ot = i;	// need this to satisfy lambda's need for a final variable
				ToneControl tc = tca[i];
				tc.getAmplitude().addListener((obs, then, now) -> amps.set(ot, now.doubleValue()));
			}
		} catch (LineUnavailableException e) {
			System.err.println("Problem setting up audio output, playback will be disabled.");
		}
	}
	
	public void stop() {
		playing = false;
	}
	
	private void start(SourceDataLine line) {
		playing = true;
		while (playing) {
			// TODO
		}
	}
}
