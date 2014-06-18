package examples.tankarena;

import java.util.ArrayList;

import utils.physics.Vector;
import engine.core.execute.Server;
import engine.core.implementation.behavior.activity.BehaviorActivity;
import engine.core.implementation.network.base.decoding.DecoderMapper;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.rendering.activities.AnimationActivity;
import engine.core.network.NetworkInputHub;
import engine.core.network.NetworkInputTrigger;
import engine.core.network.message.MessageDeclaration;
import engine.core.network.message.command.CommandInterpreter;
import engine.inputs.InputManager;
import examples.tankarena.entities.tank.TankBody;
import examples.tankarena.entities.tank.TankGun;
import examples.tankarena.entities.tank.TankTread;

public class ArenaServer extends Server {
	private PhysicsActivity m_physics;
	private BehaviorActivity m_behavior;
	private AnimationActivity m_animation;
	private TankBody m_testBody;

	public ArenaServer(CommandInterpreter interpreter, int port) {
		super(interpreter, port, new DecoderMapper(), "images-all.txt");

		m_physics = new PhysicsActivity(this.getEntitySystem());
		m_behavior = new BehaviorActivity(this.getEntitySystem());
		m_animation = new AnimationActivity(this.getEntitySystem());
		this.setUPS(50);
		this.setUPF(1);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onStop() {
	}

	@Override
	public void update(int ticks) {
		m_behavior.update(ticks);
		m_physics.update(ticks);
		m_animation.update(ticks);
	}

	@Override
	public void onClientConnect(int clientID) {
		InputManager input = makeInputManager(this.getInputHub(), clientID);
		m_testBody = new TankBody(new Vector(0, 0), new Vector(0, 0), 50, 100, "phage2dlogo.png", 1, clientID, input);
		TankTread treads1 = new TankTread(new Vector(35, 0), 20, 100, new String[] { "treads1.png", "treads2.png",
				"treads3.png", "treads4.png", "treads1.png" }, 1, input, m_testBody, m_physics, 1);
		TankTread treads2 = new TankTread(new Vector(-35, 0), 20, 100, new String[] { "treads1.png", "treads2.png",
				"treads3.png", "treads4.png", "treads1.png" }, 1, input, m_testBody, m_physics, -1);
		TankGun gun = new TankGun(15, 50, new String[] { "gun3.png" }, 2, input, m_testBody, m_physics,
				this.getEntitySystem());
		this.getEntitySystem().addEntity(m_testBody);
		this.getEntitySystem().addEntity(treads1);
		this.getEntitySystem().addEntity(treads2);
		this.getEntitySystem().addEntity(gun);
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
