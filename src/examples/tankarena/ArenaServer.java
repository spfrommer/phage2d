package examples.tankarena;

import java.util.ArrayList;

import engine.core.execute.Server;
import engine.core.implementation.behavior.activity.BehaviorActivity;
import engine.core.implementation.network.base.decoding.DecoderMapper;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.rendering.activities.AnimationActivity;
import engine.core.implementation.spawning.activities.SpawningActivity;
import engine.core.network.NetworkInputHub;
import engine.core.network.NetworkInputTrigger;
import engine.core.network.message.MessageDeclaration;
import engine.core.network.message.command.CommandInterpreter;
import engine.inputs.InputManager;
import examples.spaceship.ShaceshipServer;

public class ArenaServer extends Server {
	private PhysicsActivity m_physics;
	private BehaviorActivity m_behavior;
	private SpawningActivity m_spawning;
	private AnimationActivity m_animation;

	public ArenaServer(CommandInterpreter interpreter, int port) {
		super(interpreter, port, new DecoderMapper(), "images-all.txt");
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onStop() {
	}

	@Override
	public void initProcesses() {
		m_physics = new PhysicsActivity(this.getEntitySystem());
		m_behavior = new BehaviorActivity(this.getEntitySystem());
		m_spawning = new SpawningActivity(this.getEntitySystem());
		m_animation = new AnimationActivity(this.getEntitySystem());
	}

	@Override
	public void update(int ticks) {
		m_behavior.update(ticks);
		m_physics.update(ticks);
		m_animation.update(ticks);
		m_spawning.spawn(this.getEntitySystem(), ticks);
	}

	@Override
	public void onClientConnect(int clientID) {
		// Entity entity = makeSpaceship(new Vector(0, 0), clientID);
		// this.getEntitySystem().addEntity(entity);
	}

	private InputManager makeInputManager(NetworkInputHub inputHub, int clientID) {
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
