package sound;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class SoundPlayer implements Runnable {
	private boolean m_exitOnFinished = false;
	private Thread m_thread = new Thread(this);
	private MixingAudioInputStream m_mixer;

	private SourceDataLine m_output = null;
	
	public SoundPlayer(AudioFormat format) {
		m_output = null;
		m_mixer = new MixingAudioInputStream(format);
	}
	public SoundPlayer(AudioFormat format, boolean exitOnFinished) {
		m_exitOnFinished = exitOnFinished;
		m_mixer = new MixingAudioInputStream(format);
	}
	
	public void open(Speaker speaker) throws IOException {
		m_output = speaker.openLine(m_mixer.getFormat());
		//Make sure volume is turned up
		//Fix for pulse backend
		setVolume(1);
	}
	
	public void setVolume(float volume) {
		if (m_output == null) throw new RuntimeException("Must open to speaker before setVolume(float)");
		if (m_output.isControlSupported(FloatControl.Type.VOLUME)) {
			FloatControl control = (FloatControl) m_output.getControl(FloatControl.Type.VOLUME);
			control.setValue(volume * control.getMaximum());
		} else if (m_output.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			FloatControl gainControl = (FloatControl) m_output.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		} else throw new RuntimeException("Volume Not Supported");
	}
	public float getVolume() {
		if (m_output == null) throw new RuntimeException("Must open to speaker before getVolume()");
		if (m_output.isControlSupported(FloatControl.Type.VOLUME)) {
			FloatControl control = (FloatControl) m_output.getControl(FloatControl.Type.VOLUME);
			return control.getValue()/control.getMaximum();
		} else if (m_output.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			return 0;
		} else throw new RuntimeException("Volume not Supported");
	}
	
	public AudioFormat getFormat() { return m_mixer.getFormat(); }
	
	public void play(AudioInputStream audio) {
		if (m_output == null) throw new RuntimeException("Must open to speaker before play(AudioInputStream)");
		if (!audio.getFormat().matches(m_output.getFormat())) throw new RuntimeException("Format not compatible with Player");
		m_mixer.add(audio);
	}
	public void pause(AudioInputStream audio) {
		m_mixer.remove(audio);
	}
	
	public void start() {
		if (m_output == null) throw new RuntimeException("Must open to speaker before start()");
		m_thread.start();
	}
	public void stop() {
		m_thread.interrupt();
	}
	
	public void run() {
		m_output.start();
		byte[] data = new byte[4096];
	    int nBytesRead = 0;
	    while (nBytesRead != -1) {
	    	try {
	    		nBytesRead = m_mixer.read(data, 0, data.length);
	    		if (nBytesRead != -1) m_output.write(data, 0, nBytesRead);
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
			if (Thread.interrupted() || (m_mixer.getStreams().size() == 0 && m_exitOnFinished)) break;
	    }
	    m_output.stop();
	    m_output.close();
	}
}
