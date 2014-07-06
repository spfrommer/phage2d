package engine.sound.decode;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import engine.sound.SoundResource.SoundDecoder;

public class GenericDecoder implements SoundDecoder {
	@Override
	public AudioInputStream decode(InputStream stream) throws IOException {
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(stream);
			AudioFormat baseFormat = in.getFormat();
			AudioFormat newFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
								baseFormat.getSampleRate(),
								16,
								baseFormat.getChannels(),
								baseFormat.getChannels() * 2,
								baseFormat.getSampleRate(),
								true);
			return AudioSystem.getAudioInputStream(newFormat, in);
		} catch (UnsupportedAudioFileException e) {
			throw new IOException("Unsupported stream " + e);
		}
	}
}
