package engine.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.tritonus.share.sampled.TConversionTool;

public class MixingAudioInputStream extends AudioInputStream {
	private static final boolean DEBUG = false;

	private List<AudioInputStream> m_audioInputStreamList;

	public MixingAudioInputStream(AudioFormat audioFormat,
			Collection<? extends AudioInputStream> audioInputStreams) {
		super(new ByteArrayInputStream(new byte[0]), audioFormat,
				AudioSystem.NOT_SPECIFIED);
		m_audioInputStreamList = new ArrayList<AudioInputStream>(
				audioInputStreams);
	}

	public MixingAudioInputStream(AudioFormat audioFormat) {
		super(new ByteArrayInputStream(new byte[0]), audioFormat,
				AudioSystem.NOT_SPECIFIED);
		m_audioInputStreamList = new ArrayList<AudioInputStream>();
	}

	public void add(AudioInputStream audio) {
		m_audioInputStreamList.add(audio);
	}

	public void remove(AudioInputStream audio) {
		m_audioInputStreamList.remove(audio);
	}

	public List<AudioInputStream> getStreams() {
		return m_audioInputStreamList;
	}

	public long getFrameLength() {
		long lLengthInFrames = 0;
		Iterator<AudioInputStream> streamIterator = m_audioInputStreamList
				.iterator();
		while (streamIterator.hasNext()) {
			AudioInputStream stream = streamIterator.next();
			long lLength = stream.getFrameLength();
			if (lLength == AudioSystem.NOT_SPECIFIED) {
				return AudioSystem.NOT_SPECIFIED;
			} else {
				lLengthInFrames = Math.max(lLengthInFrames, lLength);
			}
		}
		return lLengthInFrames;
	}

	public int read() throws IOException {
		if (DEBUG) {
			out("MixingAudioInputStream.read(): begin");
		}
		int nSample = 0;
		Iterator<AudioInputStream> streamIterator = m_audioInputStreamList
				.iterator();
		while (streamIterator.hasNext()) {
			AudioInputStream stream = streamIterator.next();
			int nByte = stream.read();
			if (nByte == -1) {
				/*
				 * The end of this stream has been signaled. We remove the
				 * stream from our list.
				 */
				streamIterator.remove();
				continue;
			} else {
				/*
				 * what about signed/unsigned?
				 */
				nSample += nByte;
			}
		}
		if (DEBUG) {
			out("MixingAudioInputStream.read(): end");
		}
		return (byte) (nSample & 0xFF);
	}

	public int read(byte[] abData, int nOffset, int nLength) throws IOException {
		int nChannels = getFormat().getChannels();
		int nFrameSize = getFormat().getFrameSize();
		/*
		 * This value is in bytes. Note that it is the storage size. It may be
		 * four bytes for 24 bit samples.
		 */
		int nSampleSize = nFrameSize / nChannels;
		boolean bBigEndian = getFormat().isBigEndian();
		AudioFormat.Encoding encoding = getFormat().getEncoding();

		byte[] abBuffer = new byte[nFrameSize];
		int[] anMixedSamples = new int[nChannels];
		for (int nFrameBoundry = 0; nFrameBoundry < nLength; nFrameBoundry += nFrameSize) {
			if (DEBUG) {
				out("MixingAudioInputStream.read(byte[], int, int): frame boundry: "
						+ nFrameBoundry);
			}
			for (int i = 0; i < nChannels; i++) {
				anMixedSamples[i] = 0;
			}
			// Iterator<AudioInputStream> streamIterator =
			// m_audioInputStreamList.iterator();
			// while (streamIterator.hasNext()) {
			// AudioInputStream stream = streamIterator.next();
			int i = 0;
			while (i < m_audioInputStreamList.size()) {
				AudioInputStream stream = m_audioInputStreamList.get(i);
				if (DEBUG) {
					out("MixingAudioInputStream.read(byte[], int, int): AudioInputStream: "
							+ stream);
				}
				int nBytesRead = stream.read(abBuffer, 0, nFrameSize);
				if (DEBUG) {
					out("MixingAudioInputStream.read(byte[], int, int): bytes read: "
							+ nBytesRead);
				}
				/*
				 * TODO: we have to handle incomplete reads.
				 */
				if (nBytesRead == -1) {
					/*
					 * The end of the current stream has been signaled. We
					 * remove it from the list of streams.
					 */
					m_audioInputStreamList.remove(i);
					// streamIterator.remove();
					continue;
				}
				i++;
				for (int nChannel = 0; nChannel < nChannels; nChannel++) {
					int nBufferOffset = nChannel * nSampleSize;
					int nSampleToAdd = 0;
					if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
						switch (nSampleSize) {
						case 1:
							nSampleToAdd = abBuffer[nBufferOffset];
							break;
						case 2:
							nSampleToAdd = TConversionTool.bytesToInt16(
									abBuffer, nBufferOffset, bBigEndian);
							break;
						case 3:
							nSampleToAdd = TConversionTool.bytesToInt24(
									abBuffer, nBufferOffset, bBigEndian);
							break;
						case 4:
							nSampleToAdd = TConversionTool.bytesToInt32(
									abBuffer, nBufferOffset, bBigEndian);
							break;
						}
					}
					// TODO: pcm unsigned
					else if (encoding.equals(AudioFormat.Encoding.ALAW)) {
						nSampleToAdd = TConversionTool
								.alaw2linear(abBuffer[nBufferOffset]);
					} else if (encoding.equals(AudioFormat.Encoding.ULAW)) {
						nSampleToAdd = TConversionTool
								.ulaw2linear(abBuffer[nBufferOffset]);
					}
					anMixedSamples[nChannel] += nSampleToAdd;
				} // loop over channels
			} // loop over streams
			if (DEBUG) {
				out("MixingAudioInputStream.read(byte[], int, int): starting to write to buffer passed by caller");
			}
			for (int nChannel = 0; nChannel < nChannels; nChannel++) {
				if (DEBUG) {
					out("MixingAudioInputStream.read(byte[], int, int): channel: "
							+ nChannel);
				}
				int nBufferOffset = nOffset + nFrameBoundry /* * nFrameSize */
						+ nChannel * nSampleSize;
				if (DEBUG) {
					out("MixingAudioInputStream.read(byte[], int, int): buffer offset: "
							+ nBufferOffset);
				}
				if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
					switch (nSampleSize) {
					case 1:
						abData[nBufferOffset] = (byte) anMixedSamples[nChannel];
						break;
					case 2:
						TConversionTool.intToBytes16(anMixedSamples[nChannel],
								abData, nBufferOffset, bBigEndian);
						break;
					case 3:
						TConversionTool.intToBytes24(anMixedSamples[nChannel],
								abData, nBufferOffset, bBigEndian);
						break;
					case 4:
						TConversionTool.intToBytes32(anMixedSamples[nChannel],
								abData, nBufferOffset, bBigEndian);
						break;
					}
				}
				// TODO: pcm unsigned
				else if (encoding.equals(AudioFormat.Encoding.ALAW)) {
					abData[nBufferOffset] = TConversionTool
							.linear2alaw((short) anMixedSamples[nChannel]);
				} else if (encoding.equals(AudioFormat.Encoding.ULAW)) {
					abData[nBufferOffset] = TConversionTool
							.linear2ulaw(anMixedSamples[nChannel]);
				}
			} // (final) loop over channels
		} // loop over frames
		if (DEBUG) {
			out("MixingAudioInputStream.read(byte[], int, int): end");
		}
		// TODO: return a useful value
		return nLength;
	}

	/**
	 * calls skip() on all input streams. There is no way to assure that the
	 * number of bytes really skipped is the same for all input streams. Due to
	 * that, this method always returns the passed value. In other words: the
	 * return value is useless (better ideas appreciated).
	 */
	public long skip(long lLength) throws IOException {
		Iterator<AudioInputStream> streamIterator = m_audioInputStreamList
				.iterator();
		while (streamIterator.hasNext()) {
			AudioInputStream stream = streamIterator.next();
			stream.skip(lLength);
		}
		return lLength;
	}

	/**
	 * The minimum of available() of all input stream is calculated and
	 * returned.
	 */
	public int available() throws IOException {
		int nAvailable = 0;
		Iterator<AudioInputStream> streamIterator = m_audioInputStreamList
				.iterator();
		while (streamIterator.hasNext()) {
			AudioInputStream stream = streamIterator.next();
			nAvailable = Math.min(nAvailable, stream.available());
		}
		return nAvailable;
	}

	public void close() throws IOException {
		// TODO: should we close all streams in the list?
	}

	/**
	 * Calls mark() on all input streams.
	 */
	public void mark(int nReadLimit) {
		Iterator<AudioInputStream> streamIterator = m_audioInputStreamList
				.iterator();
		while (streamIterator.hasNext()) {
			AudioInputStream stream = streamIterator.next();
			stream.mark(nReadLimit);
		}
	}

	/**
	 * Calls reset() on all input streams.
	 */
	public void reset() throws IOException {
		Iterator<AudioInputStream> streamIterator = m_audioInputStreamList
				.iterator();
		while (streamIterator.hasNext()) {
			AudioInputStream stream = streamIterator.next();
			stream.reset();
		}
	}

	/**
	 * returns true if all input stream return true for markSupported().
	 */
	public boolean markSupported() {
		Iterator<AudioInputStream> streamIterator = m_audioInputStreamList
				.iterator();
		while (streamIterator.hasNext()) {
			AudioInputStream stream = streamIterator.next();
			if (!stream.markSupported()) {
				return false;
			}
		}
		return true;
	}

	private static void out(String strMessage) {
		System.out.println(strMessage);
	}
}