package examples.flipflop;

import utils.physics.Vector;
import engine.core.execute.Game;
import engine.core.framework.Entity;
import engine.core.implementation.camera.activities.CameraActivity;
import engine.core.implementation.camera.activities.KeyboardCameraActivity;
import engine.core.implementation.camera.activities.MovementProfile;
import engine.core.implementation.control.activities.ControllerActivity;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.rendering.activities.AnimationActivity;
import engine.core.implementation.rendering.activities.ParallaxRenderingActivity;
import engine.graphics.lwjgl.LWJGLKeyboard;
import engine.inputs.BindingListener;
import engine.inputs.InputManager;
import engine.inputs.keyboard.KeyTrigger;
import examples.flipflop.worlds.WorldFactory;

public class FlipFlop extends Game {
	private PhysicsActivity m_physics;
	private AnimationActivity m_animation;
	private CameraActivity m_camera;
	private ControllerActivity m_controller;

	private PortalManager m_portalManager;

	public static void main(String[] args) {
		new FlipFlop().start();
	}

	public FlipFlop() {
		super(1000, 500);
		m_portalManager = new PortalManager(this.getEntitySystem());
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
	}

	@Override
	public void onStart() {
		WorldFactory.addWorld1(this.getEntitySystem());
		loadLevel(new TestLevel());

		m_portalManager
				.addPortalsSatisfiedListener(new PortalsSatisfiedListener() {
					@Override
					public void portalsSatisfied() {
						nextLevel();
					}
				});

		this.getViewport().getCamera().setZoom(0.15);
		ParallaxRenderingActivity rendering = new ParallaxRenderingActivity(
				this.getEntitySystem(), this.getViewport().getCamera());
		rendering.loadEntities();
		this.setRenderingActivity(rendering);
		makeInputManager();
	}

	@Override
	public void onStop() {

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

	private void nextLevel() {
		this.getEntitySystem().removeAllEntities();
		m_physics.setGravity(new Vector(0, -9.8));
		m_portalManager.resetPortals();

		DynamicLevel dynamic = new DynamicLevel();
		dynamic.load(LevelReader.read("level3.lvl"));

		WorldFactory.addWorld1(getEntitySystem());
		loadLevel(dynamic);
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
				m_physics.setGravity(new Vector(0, 9.8));
			}
		});
		manager.addBindingListener("Down", new BindingListener() {
			@Override
			public void onAction(String binding, float value) {
				m_physics.setGravity(new Vector(0, -9.8));
			}
		});
		manager.addBindingListener("Right", new BindingListener() {
			@Override
			public void onAction(String binding, float value) {
				m_physics.setGravity(new Vector(9.8, 0));
			}
		});
		manager.addBindingListener("Left", new BindingListener() {
			@Override
			public void onAction(String binding, float value) {
				m_physics.setGravity(new Vector(-9.8, 0));
			}
		});
		return manager;
	}
}
