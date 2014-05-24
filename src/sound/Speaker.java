package sound;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

public interface Speaker {
	public SourceDataLine openLine(AudioFormat format) throws IOException;
	public String getName();
}
