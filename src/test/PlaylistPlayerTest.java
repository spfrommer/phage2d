package test;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

import engine.sound.PlaylistPlayer;
import engine.sound.SoundResource;
import engine.sound.SoundSystem;
import engine.sound.PlaylistPlayer.PlaylistPlayerListener;

public class PlaylistPlayerTest {
	public static void main(String[] args) throws IOException, InterruptedException {
		final SoundResource music = new SoundResource(PlaylistPlayerTest.class.getResource("/sounds/music_sample.mp3"));
		
		AudioInputStream input = music.openStream();

		PlaylistPlayer player = new PlaylistPlayer(input.getFormat());
		player.open(SoundSystem.s_getDefaultSpeaker());
		player.add(input);
		player.addListener(new PlaylistPlayerListener() {
			@Override
			public void streamEnded(PlaylistPlayer player, AudioInputStream done) {
				System.out.println("Restarting");
				try {
					player.add(music.openStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void startedPlaying(PlaylistPlayer player, AudioInputStream n) {
				
			}
			
		});

		player.start();		

		//Will pause after five seconds

		Thread.sleep(5000);
		player.pause();
		Thread.sleep(2000);
		player.play();
	}
}
