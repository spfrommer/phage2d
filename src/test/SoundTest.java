package test;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import sound.SoundResource;
import sound.SoundSystem;
import sound.decode.GenericDecoder;

public class SoundTest {
	public static void main(String[] args) throws IOException {
		SoundResource music = new SoundResource(SoundTest.class.getResource("/sounds/music_sample.mp3"), new GenericDecoder());

		SoundSystem.s_getPlayer(music, 1f).start();
	}
}
