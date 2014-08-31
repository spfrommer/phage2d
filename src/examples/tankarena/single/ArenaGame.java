package examples.tankarena.single;

import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;

import utils.image.ImageUtils;
import utils.image.Texture;
import utils.physics.Vector;
import engine.core.execute.Game;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.implementation.behavior.activity.BehaviorActivity;
import engine.core.implementation.camera.activities.CameraActivity;
import engine.core.implementation.camera.activities.KeyboardCameraActivity;
import engine.core.implementation.camera.activities.MovementProfile;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.PhysicsTransformWrapper;
import engine.core.implementation.physics.wrappers.ShellTransformWrapper;
import engine.core.implementation.rendering.activities.AnimationActivity;
import engine.core.implementation.rendering.logic.TextureRenderingLogic;
import engine.graphics.Renderer;
import engine.graphics.lwjgl.LWJGLKeyboard;
import engine.inputs.InputManager;
import engine.inputs.keyboard.KeyTrigger;
import engine.inputs.mouse.MouseWorldXTrigger;
import examples.tankarena.single.entities.tank.Tank;
import examples.tankarena.single.entities.tank.body.TankBody;
import examples.tankarena.single.entities.tank.gun.TankGun;
import examples.tankarena.single.entities.tank.tread.TankTread;

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

		input.addBinding("MouseWorldX", new MouseWorldXTrigger(this.getMouse(), this.getViewPort()));
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
		addTank(makeInputManager());
		this.getEntitySystem().addEntity(makeBackground());
		this.getEntitySystem().addEntity(makeWall(new Vector(1000, 0), 20, 2000));
		this.getEntitySystem().addEntity(makeWall(new Vector(-1000, 0), 20, 2000));
		this.getEntitySystem().addEntity(makeWall(new Vector(0, 1000), 2000, 20));
		this.getEntitySystem().addEntity(makeWall(new Vector(0, -1000), 2000, 20));

		this.getEntitySystem().addEntity(makeWall(new Vector(200, 100), 200, 20));
		this.getEntitySystem().addEntity(makeWall(new Vector(60, 400), 20, 200));
		this.getEntitySystem().addEntity(makeWall(new Vector(0, -4000), 20, 200));
		this.getEntitySystem().addEntity(makeWall(new Vector(-200, 100), 200, 200));
		this.getEntitySystem().addEntity(makeWall(new Vector(300, -300), 200, 100));
		this.getEntitySystem().addEntity(makeWall(new Vector(600, 600), 400, 100));
		this.getEntitySystem().addEntity(makeWall(new Vector(600, -400), 200, 200));
		this.getEntitySystem().addEntity(makeWall(new Vector(-400, 600), 400, 400));
		this.getEntitySystem().addEntity(makeWall(new Vector(-60, 200), 400, 40));
		this.getEntitySystem().addEntity(makeWall(new Vector(-500, 0), 100, 100));
		this.getEntitySystem().addEntity(makeWall(new Vector(800, 0), 200, 40));
		this.getEntitySystem().addEntity(makeWall(new Vector(-700, -700), 100, 200));
		this.getEntitySystem().addEntity(makeWall(new Vector(-700, -400), 300, 200));
	}

	private Tank addTank(InputManager input) {
		// make the body
		TankBody.BodyBuilder bodyBuilder = new TankBody.BodyBuilder();
		bodyBuilder.setPosition(new Vector(0, 0));
		bodyBuilder.setCenter(new Vector(0, 0));
		bodyBuilder.setWidth(50);
		bodyBuilder.setHeight(100);
		bodyBuilder.setTexture("phage2dlogo.png");
		bodyBuilder.setLayer(1);
		TankBody body = bodyBuilder.build();
		this.getEntitySystem().addEntity(body);

		// make the treads
		TankTread.TreadBuilder treadBuilder = new TankTread.TreadBuilder();
		treadBuilder.setWidth(20);
		treadBuilder.setHeight(100);
		treadBuilder.setTextures(new String[] { "treads1.png", "treads2.png", "treads3.png", "treads4.png",
				"treads1.png" });
		treadBuilder.setInputManager(input);
		treadBuilder.setBody(body);
		treadBuilder.setPhysicsActivity(m_physics);
		treadBuilder.setLayer(1);
		treadBuilder.setDirection(-1);
		treadBuilder.setPosition(new Vector(-35, 0));
		TankTread tread1 = treadBuilder.build();
		this.getEntitySystem().addEntity(tread1);
		treadBuilder.setDirection(1);
		treadBuilder.setPosition(new Vector(35, 0));
		TankTread tread2 = treadBuilder.build();
		this.getEntitySystem().addEntity(tread2);

		// make the gun
		TankGun.GunBuilder gunBuilder = new TankGun.GunBuilder();
		gunBuilder.setWidth(15);
		gunBuilder.setHeight(50);
		gunBuilder.setTextures(new String[] { "gun3.png" });
		gunBuilder.setInputManager(input);
		gunBuilder.setLayer(2);
		gunBuilder.setBody(body);
		gunBuilder.setPhysicsActivity(m_physics);
		TankGun gun = gunBuilder.build();
		this.getEntitySystem().addEntity(gun);

		Tank tank = new Tank(body, tread1, tread2, gun);
		return tank;
	}

	private Entity makeBackground() {
		Entity background = new Entity();

		ComponentFactory.addShellData(background, new Vector(0, 0), 0);
		ComponentFactory.addTextureData(background, new Texture(ImageUtils.getID("tiledfloor.png"), 2000, 2000));
		ComponentFactory.addNameData(background, "background");
		ComponentFactory.addLayerData(background, 0);

		background.addComponent(new ShellTransformWrapper());
		background.addComponent(new TextureRenderingLogic());
		return background;
	}

	private Entity makeWall(Vector position, double width, double height) {
		Entity wall = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(wall, position, 0, new Rectangle(width, height));
		physics.setMassType(Mass.Type.INFINITE);

		ComponentFactory.addTextureData(wall, new Texture(ImageUtils.getID("darkmetal.jpg"), width, height));
		ComponentFactory.addNameData(wall, "wall");
		ComponentFactory.addLayerData(wall, 1);

		wall.addComponent(new PhysicsTransformWrapper());
		wall.addComponent(new TextureRenderingLogic());

		return wall;
	}

	@Override
	public void update(int ticks) {
		m_cam.control(this.getViewPort().getCamera(), ticks);

		m_behavior.update(ticks);
		m_physics.update(ticks);
		m_animation.update(ticks);
	}

	@Override
	public void onRender(Renderer renderer) {

	}

	public static void main(String[] args) {
		ArenaGame arena = new ArenaGame();
		arena.start();
	}
}
