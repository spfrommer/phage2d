package examples.spaceship;

import java.util.ArrayList;

import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;

import utils.image.ImageUtils;
import utils.image.Texture;
import utils.physics.Vector;
import engine.core.execute.Server;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.camera.data.CameraFocusData;
import engine.core.implementation.control.activities.ControllerActivity;
import engine.core.implementation.control.logic.MouseControllerLogic;
import engine.core.implementation.extras.activities.HealthActivity;
import engine.core.implementation.network.base.decoding.DecoderMapper;
import engine.core.implementation.network.base.encoding.BasicDataEncoder;
import engine.core.implementation.network.logic.ServerLogic;
import engine.core.implementation.network.wrappers.EncoderWrapper;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.logic.filter.AllCollisionFilterLogic;
import engine.core.implementation.physics.logic.filter.ExclusiveCollisionFilterLogic;
import engine.core.implementation.physics.logic.handler.BulletCollisionHandlerLogic;
import engine.core.implementation.rendering.activities.AnimationActivity;
import engine.core.implementation.spawning.activities.SpawningActivity;
import engine.core.implementation.spawning.logic.GunLogic;
import engine.core.network.NetworkInputHub;
import engine.core.network.NetworkInputTrigger;
import engine.core.network.message.MessageDeclaration;
import engine.core.network.message.command.CommandInterpreter;
import engine.inputs.InputManager;

public class ShaceshipServer extends Server {
	private PhysicsActivity m_physics;
	private ControllerActivity m_control;
	private SpawningActivity m_spawning;
	private AnimationActivity m_animation;
	private HealthActivity m_health;

	public ShaceshipServer(CommandInterpreter interpreter, int port) {
		super(interpreter, port, new DecoderMapper());
	}

	@Override
	public void onStart() {
		this.getEntitySystem().addEntity(makeBackground());

		this.getEntitySystem().addEntity(makeWall(new Vector(5050, 0), 100, 10000));
		this.getEntitySystem().addEntity(makeWall(new Vector(-5050, 0), 100, 10000));
		this.getEntitySystem().addEntity(makeWall(new Vector(0, 5050), 10000, 100));
		this.getEntitySystem().addEntity(makeWall(new Vector(0, -5050), 10000, 100));

		this.getEntitySystem().addEntity(makeWall(new Vector(1000, 550), 1000, 100));
		this.getEntitySystem().addEntity(makeWall(new Vector(300, 2050), 100, 800));
		this.getEntitySystem().addEntity(makeWall(new Vector(0, -1900), 100, 1000));
		this.getEntitySystem().addEntity(makeWall(new Vector(-1000, 500), 1000, 1000));
		this.getEntitySystem().addEntity(makeWall(new Vector(1500, -1500), 900, 500));
		this.getEntitySystem().addEntity(makeWall(new Vector(3000, 3000), 2000, 500));
		this.getEntitySystem().addEntity(makeWall(new Vector(3000, -2000), 1000, 1000));
		this.getEntitySystem().addEntity(makeWall(new Vector(-2000, 3000), 2000, 2000));
		this.getEntitySystem().addEntity(makeWall(new Vector(-3300, 1000), 2000, 200));
		this.getEntitySystem().addEntity(makeWall(new Vector(-2500, 0), 500, 500));
		this.getEntitySystem().addEntity(makeWall(new Vector(4000, 0), 1000, 200));
		this.getEntitySystem().addEntity(makeWall(new Vector(-3700, -3700), 500, 1000));
		this.getEntitySystem().addEntity(makeWall(new Vector(-3700, -2000), 1500, 1000));
	}

	@Override
	public void onStop() {
	}

	@Override
	public void initProcesses() {
		m_physics = new PhysicsActivity(this.getEntitySystem());
		m_control = new ControllerActivity(this.getEntitySystem());
		m_spawning = new SpawningActivity(this.getEntitySystem());
		m_animation = new AnimationActivity(this.getEntitySystem());
		m_health = new HealthActivity(this.getEntitySystem());
	}

	@Override
	public void updateProcesses(int ticks) {
		m_control.update(ticks);
		m_physics.update(ticks);
		m_animation.update(ticks);
		m_health.update(ticks);
		m_spawning.spawn(this.getEntitySystem(), ticks);
	}

	@Override
	public void onClientConnect(int clientID) {
		Entity entity = makeBumperCar(new Vector(0, 0), clientID);
		this.getEntitySystem().addEntity(entity);
	}

	private Entity makeBackground() {
		Entity background = new Entity();
		ComponentFactory.addShellData(background, new Vector(0, 0), 0);
		ComponentFactory.addTextureData(background, new Texture(ImageUtils.getID("terrain1.jpg"), 10000, 10000));
		ComponentFactory.addNetworkData(background);
		ComponentFactory.addShellWrappers(background);
		ComponentFactory.addNameData(background, "background");
		ComponentFactory.addLayerData(background, 0);
		background.addComponent(ComponentFactory.createBasicEncoder(background));
		background.addComponent(new ServerLogic(background));
		return background;
	}

	private Entity makeWall(Vector position, double width, double height) {
		Entity wall = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(wall, position, 0, new Rectangle(width, height));
		physics.setMassType(Mass.Type.INFINITE);

		ComponentFactory.addTextureData(wall, new Texture(ImageUtils.getID("darkmetal.jpg"), width, height));
		ComponentFactory.addNetworkData(wall);
		ComponentFactory.addNameData(wall, "wall");
		ComponentFactory.addPhysicsWrappers(wall);
		ComponentFactory.addLayerData(wall, 1);
		wall.addComponent(ComponentFactory.createBasicEncoder(wall));

		wall.addComponent(new ServerLogic(wall));

		return wall;
	}

	private static String[] chassisImages = new String[] { "gun1.png", "chassis2.png", "gun2.png", "bullet1.png" };

	private Entity makeBumperCar(Vector position, int clientID) {
		Entity bumperCar = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(bumperCar, position, 0, new Rectangle(140, 250));
		physics.setRotationalFriction(1);
		physics.setMovementFriction(1);
		physics.setMass(8000);

		int imageID = ImageUtils.getID(chassisImages[clientID]);
		ComponentFactory.addTextureData(bumperCar, new Texture(imageID, 140, 250));

		ComponentFactory.addNetworkData(bumperCar);
		ComponentFactory.addHealthData(bumperCar, 200);
		ComponentFactory.addNameData(bumperCar, "bumper");
		ComponentFactory.addLayerData(bumperCar, 1);
		ComponentFactory.addCameraFocusData(bumperCar, clientID);
		ComponentFactory.addPhysicsWrappers(bumperCar);
		EncoderWrapper encoder = ComponentFactory.createBasicEncoder(bumperCar);
		encoder.addDataEncoder(TypeManager.getType(CameraFocusData.class), new BasicDataEncoder());
		bumperCar.addComponent(encoder);

		bumperCar.addComponent(new ServerLogic(bumperCar));

		InputManager input = makeInputManager(getInputHub(), clientID);

		bumperCar.addComponent(new MouseControllerLogic(bumperCar, input));

		bumperCar.addComponent(new GunLogic(bumperCar, makeBullet(bumperCar, 5), input, 10, 3000));

		bumperCar.addComponent(new AllCollisionFilterLogic(bumperCar));

		return bumperCar;
	}

	private Entity makeBullet(Entity exclude, double bulletDamage) {
		Entity bullet = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(bullet, new Vector(0, 0), 0, new Rectangle(10, 30));
		physics.setMass(1000);

		ComponentFactory.addTextureData(bullet, new Texture(ImageUtils.getID("bullet1.png"), 10, 30));
		ComponentFactory.addNetworkData(bullet);
		ComponentFactory.addDamageData(bullet, bulletDamage);
		ComponentFactory.addNameData(bullet, "bullet");
		ComponentFactory.addLayerData(bullet, 1);
		ComponentFactory.addPhysicsWrappers(bullet);
		bullet.addComponent(ComponentFactory.createBasicEncoder(bullet));

		bullet.addComponent(new ServerLogic(bullet));

		bullet.addComponent(new ExclusiveCollisionFilterLogic(bullet, exclude));

		bullet.addComponent(new BulletCollisionHandlerLogic(bullet, this.getEntitySystem()));

		return bullet;
	}

	public InputManager makeInputManager(NetworkInputHub inputHub, int clientID) {
		InputManager networkInputManager = new InputManager();

		addInputTrigger(networkInputManager, inputHub, "inputforwards", "Forwards", clientID);
		addInputTrigger(networkInputManager, inputHub, "inputbackwards", "Backwards", clientID);
		addInputTrigger(networkInputManager, inputHub, "inputright", "Right", clientID);
		addInputTrigger(networkInputManager, inputHub, "inputleft", "Left", clientID);
		addInputTrigger(networkInputManager, inputHub, "inputT", "t", clientID);

		addInputTrigger(networkInputManager, inputHub, "inputmousex", "MouseWorldX", clientID);
		addInputTrigger(networkInputManager, inputHub, "inputmousey", "MouseWorldY", clientID);
		addInputTrigger(networkInputManager, inputHub, "inputleftmousedown", "LeftMouse", clientID);
		addInputTrigger(networkInputManager, inputHub, "inputrightmousedown", "RightMouse", clientID);

		return networkInputManager;
	}

	private void addInputTrigger(InputManager inputManager, NetworkInputHub inputHub, String message, String binding,
			int id) {
		NetworkInputTrigger trigger = new NetworkInputTrigger(id, message);
		inputManager.addBinding(binding, trigger);
		inputHub.addNetworkReceiver(trigger);
	}

	public static void main(String[] args) {
		ArrayList<MessageDeclaration> declarations = new ArrayList<MessageDeclaration>();
		declarations.add(new MessageDeclaration("inputforwards"));
		declarations.add(new MessageDeclaration("inputbackwards"));
		declarations.add(new MessageDeclaration("inputright"));
		declarations.add(new MessageDeclaration("inputleft"));
		declarations.add(new MessageDeclaration("inputT"));

		declarations.add(new MessageDeclaration("inputmousex"));
		declarations.add(new MessageDeclaration("inputmousey"));
		declarations.add(new MessageDeclaration("inputleftmousedown"));
		declarations.add(new MessageDeclaration("inputrightmousedown"));
		ShaceshipServer server = new ShaceshipServer(new CommandInterpreter(declarations), Server.PORT);
		server.start();
	}
}
