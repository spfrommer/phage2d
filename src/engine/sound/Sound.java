package engine.sound;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

public interface Sound {
	public AudioInputStream openStream() throws IOException;
}
