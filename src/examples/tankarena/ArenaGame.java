package examples.tankarena;

import utils.physics.Vector;
import engine.core.execute.Game;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.activity.BehaviorActivity;
import engine.core.implementation.camera.activities.CameraActivity;
import engine.core.implementation.camera.activities.KeyboardCameraActivity;
import engine.core.implementation.camera.activities.MovementProfile;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.rendering.activities.AnimationActivity;
import engine.graphics.Renderer;
import engine.graphics.lwjgl.LWJGLKeyboard;
import engine.inputs.InputManager;
import engine.inputs.keyboard.KeyTrigger;
import examples.tankarena.entities.tank.body.TankBody;

public class ArenaGame extends Game {
	private CameraActivity m_cam;
	private PhysicsActivity m_physics;
	private BehaviorActivity m_behavior;
	private AnimationActivity m_animation;

	private TankBody m_tank;

	public ArenaGame() {
		super(1000, 1000, "images-all.txt");
	}

	private InputManager makeInputManager() {
		LWJGLKeyboard keyboard = LWJGLKeyboard.instance();
		InputManager input = new InputManager();
		input.addBinding("Forwards", new KeyTrigger(keyboard.getKey('i')));
		input.addBinding("Backwards", new KeyTrigger(keyboard.getKey('k')));
		input.addBinding("Left", new KeyTrigger(keyboard.getKey('j')));
		input.addBinding("Right", new KeyTrigger(keyboard.getKey('l')));
		return input;
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onStop() {

	}

	@Override
	public void init() {
		m_cam = new KeyboardCameraActivity(this.getEntitySystem(), LWJGLKeyboard.instance(), new MovementProfile(20,
				0.05));
		m_physics = new PhysicsActivity(this.getEntitySystem());
		m_behavior = new BehaviorActivity(this.getEntitySystem());
		m_animation = new AnimationActivity(this.getEntitySystem());

		InputManager input = makeInputManager();
		// m_tank = new TankBody(new Vector(0, 0), new Vector(0, 0), 50, 100, "chassis1.png", 1, 0, input);
		/*TankTread treads1 = new TankTread(new Vector(35, 0), 20, 100, new String[] { "treads1.png", "treads2.png",
				"treads3.png", "treads4.png", "treads1.png" }, 1, input, m_tank, m_physics, 1);
		TankTread treads2 = new TankTread(new Vector(-35, 0), 20, 100, new String[] { "treads1.png", "treads2.png",
				"treads3.png", "treads4.png", "treads1.png" }, 1, input, m_tank, m_physics, -1);*/
		// TankGun gun = new TankGun(15, 50, new String[] { "gun1.png" }, 2, input, body, m_physics);
		this.getEntitySystem().addEntity(m_tank);
		// this.getEntitySystem().addEntity(treads1);
		// this.getEntitySystem().addEntity(treads2);
		// this.getEntitySystem().addEntity(gun);
	}

	private long m_lastTime = System.currentTimeMillis();

	@Override
	public void update(int ticks) {
		System.out.println(System.currentTimeMillis() - m_lastTime);
		m_lastTime = System.currentTimeMillis();
		m_cam.control(this.getViewPort().getCamera(), ticks);

		m_behavior.update(ticks);
		m_physics.update(ticks);
		m_animation.update(ticks);

		PhysicsData data = (PhysicsData) m_tank.getComponent(TypeManager.typeOf(PhysicsData.class));
		data.setPosition(data.getPosition().add(new Vector(0, 1)));
	}

	@Override
	public void onRender(Renderer renderer) {

	}

	public static void main(String[] args) {
		ArenaGame arena = new ArenaGame();
		arena.start();
	}
}
