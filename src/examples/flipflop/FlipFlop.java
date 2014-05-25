package examples.flipflop;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import sound.PlaylistPlayer;
import sound.PlaylistPlayer.PlaylistPlayerListener;
import sound.SoundResource;
import sound.SoundSystem;
import sound.decode.GenericDecoder;
import test.SoundTest;
import utils.physics.Vector;
import engine.core.execute.Game;
import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.SystemAspectManager;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.camera.activities.CameraActivity;
import engine.core.implementation.camera.activities.KeyboardCameraActivity;
import engine.core.implementation.camera.activities.MovementProfile;
import engine.core.implementation.camera.base.Camera;
import engine.core.implementation.control.activities.ControllerActivity;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.rendering.activities.AnimationActivity;
import engine.core.implementation.rendering.activities.ParallaxRenderingActivity;
import engine.graphics.Color;
import engine.graphics.Renderer;
import engine.graphics.font.BMFontXMLLoader;
import engine.graphics.font.Font;
import engine.graphics.lwjgl.LWJGLKeyboard;
import engine.inputs.BindingListener;
import engine.inputs.InputManager;
import engine.inputs.keyboard.KeyTrigger;

public class FlipFlop extends Game {
	private PhysicsActivity m_physics;
	private AnimationActivity m_animation;
	private ControllerActivity m_controller;
	private CameraActivity m_camera;

	private static final double DEFAULT_CAM_ZOOM = 0.15;

	private PortalManager m_portalManager;

	private SoundResource m_waterDrop;
	private SoundResource m_wind;
	private SoundResource m_rain;
	private SoundResource m_techno;
	private SoundResource m_city;

	// game states
	private boolean m_goToNextLevel = false;
	private int m_nextLevel = 0;
	int m_lastWorld = 0;
	private ArrayList<WorldListener> m_listeners = new ArrayList<WorldListener>();

	// for fading message
	private Vector m_position;
	private String m_string;
	private double m_fade;
	private static Font font = null;

	private interface WorldListener {
		public void worldChanged();
	}

	public static void main(String[] args) {
		new FlipFlop().start();
	}

	public FlipFlop() {
		super(1000, 500, "images-flipflop.txt");
		m_portalManager = new PortalManager(this.getEntitySystem());
	}

	@Override
	public void onInit() {
		m_waterDrop = new SoundResource(
				SoundTest.class.getResource("/sounds/waterdrop.mp3"),
				new GenericDecoder());
		m_wind = new SoundResource(
				SoundTest.class.getResource("/sounds/wind.mp3"),
				new GenericDecoder());
		m_rain = new SoundResource(
				SoundTest.class.getResource("/sounds/rain.mp3"),
				new GenericDecoder());
		m_techno = new SoundResource(
				SoundTest.class.getResource("/sounds/techno.wav"),
				new GenericDecoder());
		m_city = new SoundResource(
				SoundTest.class.getResource("/sounds/city.mp3"),
				new GenericDecoder());
	}

	@Override
	public void initProcesses() {
		m_physics = new PhysicsActivity(this.getEntitySystem());
		m_animation = new AnimationActivity(this.getEntitySystem());
		m_camera = new KeyboardCameraActivity(this.getEntitySystem(),
				LWJGLKeyboard.instance(), new MovementProfile(10, 0.01));
		m_controller = new ControllerActivity(this.getEntitySystem());
	}

	@Override
	public void updateProcesses(int ticks) {
		m_physics.update(ticks);
		m_animation.update(ticks);
		m_camera.control(this.getViewport().getCamera(), ticks);
		m_controller.update(ticks);
		if (m_goToNextLevel) {
			startNextLevel();
			m_goToNextLevel = false;
		}
	}

	@Override
	public void onStart() {
		this.getViewport().getCamera().setZoom(DEFAULT_CAM_ZOOM);
		startNextLevel();

		m_portalManager
				.addPortalsSatisfiedListener(new PortalsSatisfiedListener() {
					@Override
					public void portalsSatisfied() {
						Thread t = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								m_goToNextLevel = true;
							}
						});
						t.start();
					}
				});
		ParallaxRenderingActivity rendering = new ParallaxRenderingActivity(
				this.getEntitySystem(), this.getViewport().getCamera());
		rendering.loadEntities();
		this.setRenderingActivity(rendering);
		makeInputManager();
	}

	@Override
	public void onStop() {

	}

	private PlaylistPlayer m_worldSound;

	private void loadWorld(int number) {
		if (number != m_lastWorld) {
			for (WorldListener listener : m_listeners)
				listener.worldChanged();
			m_lastWorld = number;
		}

		if (number == 0) {
			WorldFactory.setWorld0(getEntitySystem());
			m_worldSound = loopAudio(m_rain);

		}
		if (number == 1) {
			WorldFactory.setWorld1(getEntitySystem());
			m_worldSound = loopAudio(m_wind);
		}
		if (number == 2) {
			WorldFactory.setWorld2(getEntitySystem());
			m_worldSound = loopAudio(m_techno);
		}
		if (number == 3) {
			WorldFactory.setWorld3(getEntitySystem());
			m_worldSound = loopAudio(m_city);
		}

		m_listeners.add(new WorldListener() {
			@Override
			public void worldChanged() {
				m_worldSound.pause();
			}
		});
	}

	private PlaylistPlayer loopAudio(final SoundResource resource) {
		AudioInputStream input = null;
		try {
			input = resource.openStream();

			PlaylistPlayer player = new PlaylistPlayer(input.getFormat());
			player.open(SoundSystem.s_getDefaultSpeaker());
			player.add(input);
			player.addListener(new PlaylistPlayerListener() {
				@Override
				public void streamEnded(PlaylistPlayer player,
						AudioInputStream done) {
					try {
						player.add(resource.openStream());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void startedPlaying(PlaylistPlayer player,
						AudioInputStream n) {

				}

			});
			player.start();
			return player;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private void loadLevel(Level level) {
		for (Entity portal : level.getPortals(m_portalManager)) {
			m_portalManager.addPortal(portal);
			this.getEntitySystem().addEntity(portal);
		}

		for (Entity platform : level.getPlatforms())
			this.getEntitySystem().addEntity(platform);

		for (Entity ball : level.getBalls())
			this.getEntitySystem().addEntity(ball);
	}

	private void startNextLevel() {
		this.getEntitySystem().removeAllEntities();
		m_physics.setGravity(new Vector(0, -9.8));
		m_portalManager.resetPortals();

		int world = (int) (m_nextLevel / 3);
		loadWorld(world);

		if (!LevelReader.levelExists("level" + m_nextLevel + ".lvl")) {
			fadingMessage("You Win!");
			return;
		}

		fadingMessage("World " + (world + 1) + " Level "
				+ (m_nextLevel % 3 + 1));
		DynamicLevel dynamic = new DynamicLevel();
		try {
			dynamic.load(LevelReader.read("level" + m_nextLevel + ".lvl"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		loadLevel(dynamic);
		m_nextLevel++;

		zoomCam();
	}

	private void zoomCam() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Camera cam = FlipFlop.this.getViewport().getCamera();
					cam.setZoom(0.09);

					Thread.sleep(1500);

					while (cam.getZoom() < DEFAULT_CAM_ZOOM) {
						Thread.sleep(10);
						cam.incrementZoom(0.0015);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	private void fadingMessage(String text) {
		m_position = new Vector(450, 250);
		m_string = text;
		m_fade = 1;

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					while (m_fade > 0) {
						m_fade -= 0.02;
						Thread.sleep(50);
					}
					m_string = null;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	@Override
	public void renderGui(Renderer renderer) {
		String string = m_string;
		if (m_position != null && string != null) {
			try {
				if (font == null) {
					font = BMFontXMLLoader.loadFonts(
							new File(FlipFlop.class.getResource(
									"/themes/basic/ptsans.fnt").toURI()))
							.get(0);
				}
				renderer.setFont(font);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			renderer.setColor(new Color(1, 1, 1, (float) m_fade));
			renderer.drawString(string, (int) m_position.getX(),
					(int) m_position.getY());
		}
	}

	private boolean velocitiesZero() {
		SystemAspectManager manager = this.getEntitySystem().getAspectManager();
		manager.loadAspect(new Aspect(TypeManager.getType(PhysicsData.class)));
		for (Entity e : manager.getEntities(new Aspect(TypeManager
				.getType(PhysicsData.class)))) {
			PhysicsData data = (PhysicsData) e.getComponent(TypeManager
					.getType(PhysicsData.class));
			if (data.getVelocity().length() > 150)
				return false;
		}
		return true;

	}

	private InputManager makeInputManager() {
		InputManager manager = new InputManager();
		manager.addBinding("Up", new KeyTrigger(LWJGLKeyboard.instance()
				.getKey('i')));
		manager.addBinding("Down", new KeyTrigger(LWJGLKeyboard.instance()
				.getKey('k')));
		manager.addBinding("Right", new KeyTrigger(LWJGLKeyboard.instance()
				.getKey('l')));
		manager.addBinding("Left", new KeyTrigger(LWJGLKeyboard.instance()
				.getKey('j')));

		manager.addBindingListener("Up", new BindingListener() {
			@Override
			public void onAction(String binding, float value) {
				if (value > 0.1 && velocitiesZero()) {
					m_physics.setGravity(new Vector(0, 9.8));
					playBallSound();
				}
			}
		});
		manager.addBindingListener("Down", new BindingListener() {
			@Override
			public void onAction(String binding, float value) {
				if (value > 0.1 && velocitiesZero()) {
					m_physics.setGravity(new Vector(0, -9.8));
					playBallSound();
				}
			}
		});
		manager.addBindingListener("Right", new BindingListener() {
			@Override
			public void onAction(String binding, float value) {
				if (value > 0.1 && velocitiesZero()) {
					m_physics.setGravity(new Vector(9.8, 0));
					playBallSound();
				}
			}
		});
		manager.addBindingListener("Left", new BindingListener() {
			@Override
			public void onAction(String binding, float value) {
				if (value > 0.1 && velocitiesZero()) {
					m_physics.setGravity(new Vector(-9.8, 0));
					playBallSound();
				}
			}
		});
		return manager;
	}

	private void playBallSound() {
		try {
			SoundSystem.s_getPlayer(m_waterDrop, 0.5f).start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
