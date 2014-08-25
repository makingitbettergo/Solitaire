package solitaire.sound_play;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Play background music
 * 
 * @author Yue
 */
public class SolitaireSoundPlayer {
	Clip clip;
	AudioInputStream is;

	public SolitaireSoundPlayer() {

		URL url = ClassLoader.getSystemResource("solitaire/res/sound/aquarium.wav");
		try {
			is = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(is);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	public void soundPlay() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
	}

	public void terminatePlay() {
		clip.close();
		clip.flush();
	}
}