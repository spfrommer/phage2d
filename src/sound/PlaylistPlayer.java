package sound;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;
//TODO: Paused, next support
public class PlaylistPlayer implements Runnable {
	private HashSet<PlaylistPlayerListener> m_listeners = new HashSet<PlaylistPlayerListener>();
	private Thread m_thread = new Thread(this);
	
	private SourceDataLine m_output = null;
	private AudioFormat m_format = null; 
	LinkedBlockingQueue<AudioInputStream> m_queue = new LinkedBlockingQueue<AudioInputStream>();
	
	private boolean m_paused = false;
	private Object m_pausedWait = new Object();
	
	public PlaylistPlayer(AudioFormat format) {
		m_format = format;
	}
	public void addListener(PlaylistPlayerListener l) { m_listeners.add(l); }
	public void removeListener(PlaylistPlayerListener l) { m_listeners.remove(l); }
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
	
	public void add(AudioInputStream stream) {
		if (!stream.getFormat().matches(m_format)) throw new RuntimeException("Format mismatch in PlayListPlayer");
		m_queue.add(stream);
	}
	public void remove(AudioInputStream stream) {
		m_queue.remove(stream);
	}
	
	public void open(Speaker speaker) throws IOException {
		m_output = speaker.openLine(m_format);
	}
	public void pause() {
		m_paused = true;
	}
	public void play() {
		m_paused = false;
		synchronized(m_pausedWait) {
			m_pausedWait.notifyAll();
		}
	}
	
	public void start() {
		m_thread.start();
	}
	public void stop() {
		m_thread.interrupt();
	}
	

	@Override
	public void run() {
		m_output.start();
		while(true) {
			AudioInputStream input;
			try {
				input = m_queue.take();
			} catch (InterruptedException e) { break; }
			
			
			for (PlaylistPlayerListener l : m_listeners) l.startedPlaying(this, input);
			
			byte[] data = new byte[4096];
			int nBytesRead = 0;

			while (nBytesRead != -1) {
				try {
					nBytesRead = input.read(data, 0, data.length);
					if (nBytesRead != -1) m_output.write(data, 0, nBytesRead);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (m_paused) {
					synchronized(m_pausedWait) {
						try {
							m_pausedWait.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if (Thread.interrupted()) break;
			}
			
			
			for (PlaylistPlayerListener l : m_listeners) l.streamEnded(this, input);

			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (Thread.interrupted()) break;
		}
		m_output.stop();
	}
	public interface PlaylistPlayerListener {
		public void streamEnded(PlaylistPlayer player, AudioInputStream done);
		public void startedPlaying(PlaylistPlayer player, AudioInputStream n);
	}
}
