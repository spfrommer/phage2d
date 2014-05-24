package sound;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class SoundSystem {
	private static DefaultSpeaker s_default = new DefaultSpeaker();

	public static Speaker s_getDefaultSpeaker() { return s_default; }
	public static Speaker[] s_getAllSpeakers() {
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		Speaker[] speakers = new Speaker[mixerInfos.length + 1];
		for (int i = 0; i < mixerInfos.length; i++) {
			speakers[i] = new GenericSpeaker(AudioSystem.getMixer(mixerInfos[i]), mixerInfos[i].getName());
		}
		//Set the last speaker to the default
		speakers[speakers.length - 1] = s_default;
		return speakers;
	}
	public static SoundPlayer s_getPlayer(Speaker s, Sound sound) throws IOException {
		AudioInputStream input = sound.openStream();
		SoundPlayer player = new SoundPlayer(input.getFormat(), true);
		player.open(s);
		player.play(input);
		return player;
	}
	public static SoundPlayer s_getPlayer(Speaker s, Sound sound, float volume) throws IOException {
		AudioInputStream input = sound.openStream();
		SoundPlayer player = new SoundPlayer(input.getFormat(), true);
		player.open(s);
		player.setVolume(volume);
		player.play(input);
		return player;
	}
	public static SoundPlayer s_getPlayer(Sound sound) throws IOException {
		AudioInputStream input = sound.openStream();
		SoundPlayer player = new SoundPlayer(input.getFormat(), true);
		player.open(s_default);
		player.play(input);
		return player;
	}
	public static SoundPlayer s_getPlayer(Sound sound, float volume) throws IOException {
		AudioInputStream input = sound.openStream();
		SoundPlayer player = new SoundPlayer(input.getFormat(), true);
		player.open(s_default);
		player.setVolume(volume);
		player.play(input);
		return player;
	}
	private static class DefaultSpeaker implements Speaker {
		public SourceDataLine openLine(AudioFormat audioFormat) throws IOException {
			SourceDataLine res = null;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
			try {
				res = (SourceDataLine) AudioSystem.getLine(info);
				res.open(audioFormat);
			} catch (Exception e) {
				throw new IOException("Unable to open line", e);
			}
			return res;
		}
		public String getName() { return "Default"; }
	}
	private	 static class GenericSpeaker implements Speaker {
		private Mixer m_mixer;
		private String m_name;
		public GenericSpeaker(Mixer mixer, String name) {
			m_mixer = mixer;
			m_name = name;
		}
		@Override
		public SourceDataLine openLine(AudioFormat format) throws IOException {
			SourceDataLine res = null;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			try {
				res = (SourceDataLine) m_mixer.getLine(info);
				res.open(format);
			} catch (Exception e) {
				throw new IOException("Unable to open line", e);
			}
			return res;
		}
		@Override
		public String getName() { return m_name; }
	}
}
