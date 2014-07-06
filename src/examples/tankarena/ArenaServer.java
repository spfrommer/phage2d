package examples.tankarena;

import java.util.ArrayList;

import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;

import utils.image.ImageUtils;
import utils.image.Texture;
import utils.physics.Vector;
import engine.core.execute.Server;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.implementation.behavior.activity.BehaviorActivity;
import engine.core.implementation.network.base.communication.NetworkInputHub;
import engine.core.implementation.network.base.communication.NetworkInputTrigger;
import engine.core.implementation.network.base.communication.message.MessageDeclaration;
import engine.core.implementation.network.base.communication.message.command.CommandInterpreter;
import engine.core.implementation.network.base.decoding.DecoderMapper;
import engine.core.implementation.network.logic.ServerLogic;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.PhysicsTransformWrapper;
import engine.core.implementation.physics.wrappers.ShellTransformWrapper;
import engine.core.implementation.rendering.activities.AnimationActivity;
import engine.inputs.InputManager;
import examples.tankarena.entities.tank.Tank;
import examples.tankarena.entities.tank.body.TankBody;
import examples.tankarena.entities.tank.gun.TankGun;
import examples.tankarena.entities.tank.tread.TankTread;

public class ArenaServer extends Server {
	private PhysicsActivity m_physics;
	private BehaviorActivity m_behavior;
	private AnimationActivity m_animation;

	public ArenaServer(CommandInterpreter interpreter, int port) {
		super(interpreter, port, new DecoderMapper(), "images-all.txt");

		m_physics = new PhysicsActivity(this.getEntitySystem());
		m_behavior = new BehaviorActivity(this.getEntitySystem());
		m_animation = new AnimationActivity(this.getEntitySystem());
		this.setUPS(40);
		this.setUPT(1);
	}

	@Override
	public void onStart() {
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

	@Override
	public void onStop() {
	}

	@Override
	public void update(int ticks) {
		m_physics.update(ticks);
		m_behavior.update(ticks);
		m_animation.update(ticks);
	}

	@Override
	public void onClientConnect(int clientID) {
		InputManager input = makeInputManager(this.getInputHub(), clientID);

		// make the body
		TankBody.BodyBuilder bodyBuilder = new TankBody.BodyBuilder();
		bodyBuilder.setPosition(new Vector(0, 0));
		bodyBuilder.setCenter(new Vector(0, 0));
		bodyBuilder.setWidth(50);
		bodyBuilder.setHeight(100);
		bodyBuilder.setTexture("phage2dlogo.png");
		bodyBuilder.setLayer(1);
		bodyBuilder.setFocusID(clientID);
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
	}

	private InputManager makeInputManager(NetworkInputHub inputHub, int clientID) {
		InputManager networkInputManager = new InputManager();

		addInputTrigger(networkInputManager, inputHub, "inputforwards", "Forwards", clientID);
		addInputTrigger(networkInputManager, inputHub, "inputbackwards", "Backwards", clientID);
		addInputTrigger(networkInputManager, inputHub, "inputright", "Right", clientID);
		addInputTrigger(networkInputManager, inputHub, "inputleft", "Left", clientID);

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

	private Entity makeBackground() {
		Entity background = new Entity();

		ComponentFactory.addShellData(background, new Vector(0, 0), 0);
		ComponentFactory.addTextureData(background, new Texture(ImageUtils.getID("tiledfloor.png"), 2000, 2000));
		ComponentFactory.addNetworkData(background);
		ComponentFactory.addNameData(background, "background");
		ComponentFactory.addLayerData(background, 0);

		background.addComponent(new ShellTransformWrapper());
		background.addComponent(ComponentFactory.createBasicEncoder(background));
		background.addComponent(new ServerLogic());
		return background;
	}

	private Entity makeWall(Vector position, double width, double height) {
		Entity wall = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(wall, position, 0, new Rectangle(width, height));
		physics.setMassType(Mass.Type.INFINITE);

		ComponentFactory.addTextureData(wall, new Texture(ImageUtils.getID("darkmetal.jpg"), width, height));
		ComponentFactory.addNetworkData(wall);
		ComponentFactory.addNameData(wall, "wall");
		ComponentFactory.addLayerData(wall, 1);

		wall.addComponent(new PhysicsTransformWrapper());
		wall.addComponent(ComponentFactory.createBasicEncoder(wall));
		wall.addComponent(new ServerLogic());

		return wall;
	}

	public static void main(String[] args) {
		ArrayList<MessageDeclaration> declarations = new ArrayList<MessageDeclaration>();
		declarations.add(new MessageDeclaration("inputforwards"));
		declarations.add(new MessageDeclaration("inputbackwards"));
		declarations.add(new MessageDeclaration("inputright"));
		declarations.add(new MessageDeclaration("inputleft"));

		declarations.add(new MessageDeclaration("inputmousex"));
		declarations.add(new MessageDeclaration("inputmousey"));
		declarations.add(new MessageDeclaration("inputleftmousedown"));
		declarations.add(new MessageDeclaration("inputrightmousedown"));
		ArenaServer server = new ArenaServer(new CommandInterpreter(declarations), Server.PORT);
		server.start();
	}
}
