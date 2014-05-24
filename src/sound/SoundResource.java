package sound;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;

import sound.decode.GenericDecoder;

public class SoundResource implements Sound {
	private URL m_resource;
	private SoundDecoder m_decoder;
	
	public SoundResource(URL resource, SoundDecoder decoder) {
		m_resource = resource;
		m_decoder = decoder;
	}
	public SoundResource(URL resource) {
		this(resource, new GenericDecoder());
	}
	
	public AudioInputStream openStream() throws IOException {
		//Open the resource
		InputStream input = m_resource.openStream();
		//Get the decoded stream
		return m_decoder.decode(input);
	}

	public interface SoundDecoder {
		public AudioInputStream decode(InputStream stream) throws IOException;
	}
}
