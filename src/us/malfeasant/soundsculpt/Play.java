package us.malfeasant.soundsculpt;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Play {
	private static final int SAMPLE_RATE = 48000;
	private static final int BYTES_PER_SAMPLE = 2;
	private static AudioFormat FORMAT =
			new AudioFormat(SAMPLE_RATE, BYTES_PER_SAMPLE * 8, 1, true, false);	// 48kHz signed 16-bit mono should be doable anywhere...
	
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
		try {
			line.open();
			line.start();
			playing = true;	// if open() fails, playing never starts
		} catch (LineUnavailableException e) {
			System.err.println("Problem opening line, playback will be disabled.");
		}
		final double timeScale = 440.0 / SAMPLE_RATE;	// fraction of a (fundamental) cycle per sample
		final byte[] buffer = new byte[2400];	// allows for 11 cycles of fundamental (smallest whole number)
		
		while (playing) {
			for (int sample = 0; sample < 1200; sample++) {
				double y = 0.0;
				for (int ot = 0; ot < amps.size(); ot++) {
					double radians = sample * timeScale * Math.PI * 2.0 * (ot + 1);
					y += Math.sin(radians) * amps.get(ot) / (ot + 1);
				}
				y *= 16384.0;	// scale: max overtones should not exceed 32767...
				buffer[sample * 2] = (byte) y;
				buffer[sample * 2 + 1] = (byte) (y / 256);
			}
			line.write(buffer, 0, buffer.length);
		}
	}
}
