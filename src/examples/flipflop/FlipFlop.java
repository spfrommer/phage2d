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

public class FlipFlop extends Game {
	private PhysicsActivity m_physics;
	private AnimationActivity m_animation;
	private CameraActivity m_camera;
	private ControllerActivity m_controller;

	private Entity m_ball1;
	private Entity m_ball2;
	private Entity m_ball3;

	private Entity m_portal1;
	private Entity m_portal2;
	private Entity m_portal3;

	private PortalManager m_portalManager = new PortalManager();

	public static void main(String[] args) {
		new FlipFlop().start();
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
		this.getEntitySystem().addEntity(
				EntityFactory.makeBackground(15000, 6000));
		addBalls();
		addPortals();
		addSquarePlatform(new Vector(-100, -100));
		addSquarePlatform(new Vector(0, -150));
		addSquarePlatform(new Vector(100, -200));

		addSquarePlatform(new Vector(200, -50));
		addSquarePlatform(new Vector(250, -100));
		addSquarePlatform(new Vector(300, -150));

		m_portalManager
				.addPortalsSatisfiedListener(new PortalsSatisfiedListener() {
					@Override
					public void portalsSatisfied() {
						nextLevel();
					}
				});

		this.getViewport().getCamera().setZoom(0.2);
		ParallaxRenderingActivity rendering = new ParallaxRenderingActivity(
				this.getEntitySystem(), this.getViewport().getCamera());
		rendering.loadEntities();
		this.setRenderingActivity(rendering);
		makeInputManager();
	}

	@Override
	public void onStop() {

	}

	private void nextLevel() {
		this.getEntitySystem().removeAllEntities();
		m_portalManager.resetPortals();

		this.getEntitySystem().addEntity(
				EntityFactory.makeBackground(15000, 6000));
		addBalls();
		addPortals();
		addSquarePlatform(new Vector(-100, -100));
		addSquarePlatform(new Vector(0, -150));
		addSquarePlatform(new Vector(100, -200));

		addSquarePlatform(new Vector(200, -50));
		addSquarePlatform(new Vector(250, -100));
		addSquarePlatform(new Vector(300, -150));
	}

	private void addSquarePlatform(Vector position) {
		this.getEntitySystem().addEntity(
				EntityFactory.makePlatform(position, 50, 50));
	}

	private void addBalls() {
		m_ball1 = EntityFactory.makeBall(new Vector(-100, 0));
		m_ball2 = EntityFactory.makeBall(new Vector(0, 0));
		m_ball3 = EntityFactory.makeBall(new Vector(100, 0));

		this.getEntitySystem().addEntity(m_ball1);
		this.getEntitySystem().addEntity(m_ball2);
		this.getEntitySystem().addEntity(m_ball3);
	}

	private void addPortals() {
		m_portal1 = EntityFactory.makePortal(new Vector(150, -300), 0,
				m_portalManager);
		m_portalManager.addPortal(m_portal1);
		m_portal2 = EntityFactory.makePortal(new Vector(200, -300), 0,
				m_portalManager);
		m_portalManager.addPortal(m_portal2);
		m_portal3 = EntityFactory.makePortal(new Vector(250, -300), 0,
				m_portalManager);
		m_portalManager.addPortal(m_portal3);

		this.getEntitySystem().addEntity(m_portal1);
		this.getEntitySystem().addEntity(m_portal2);
		this.getEntitySystem().addEntity(m_portal3);
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
