package examples.spaceship;

import java.util.ArrayList;

import engine.core.execute.Client;
import engine.core.execute.Server;
import engine.core.implementation.camera.activities.CameraActivity;
import engine.core.implementation.camera.activities.ChaseCameraActivity;
import engine.core.implementation.camera.base.ViewPort;
import engine.core.implementation.network.base.decoding.DecoderMapper;
import engine.core.implementation.network.base.decoding.ErrorDecoder;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.data.PhysicsShellData;
import engine.core.implementation.physics.data.PhysicsShellDecoder;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.data.TextureData;
import engine.core.implementation.rendering.data.TextureDecoder;
import engine.core.network.message.Message;
import engine.core.network.message.MessageDeclaration;
import engine.core.network.message.command.CommandInterpreter;
import engine.core.network.message.parameter.MessageParameter;
import engine.graphics.lwjgl.LWJGLKeyboard;
import engine.graphics.lwjgl.LWJGLMouse;
import engine.inputs.BindingListener;
import engine.inputs.InputManager;
import engine.inputs.InputTrigger;
import engine.inputs.keyboard.KeyTrigger;
import engine.inputs.mouse.MouseButton;
import engine.inputs.mouse.MouseButtonTrigger;
import engine.inputs.mouse.MouseWorldXTrigger;
import engine.inputs.mouse.MouseWorldYTrigger;

public class SpaceshipClient extends Client {
	// private ChaseCameraProcess m_chaseCam;
	private CameraActivity m_cam;

	private static DecoderMapper s_decoder = new DecoderMapper();

	static {
		s_decoder.addMapping(PhysicsData.class, new ErrorDecoder());
		s_decoder.addMapping(AnimationData.class, new ErrorDecoder());
		s_decoder.addMapping(PhysicsShellData.class, new PhysicsShellDecoder());
		s_decoder.addMapping(TextureData.class, new TextureDecoder());
	}

	public SpaceshipClient(CommandInterpreter interpreter, String server, int port) {
		super(interpreter, server, port, s_decoder);
		this.getViewPort().getCamera().setZoom(0.2);
	}

	@Override
	protected void onServerConnect() {
		createInputManager(this.getID());
	}

	@Override
	public void initProcesses() {
		m_cam = new ChaseCameraActivity(this.getEntitySystem(), this.getID());
		// m_cam = new KeyboardCameraProcess(this.getEntitySystem(), LWJGLKeyboard.instance(), new MovementProfile(10,
		// 0.01));
	}

	@Override
	public void updateProcesses(int ticks) {
		m_cam.control(this.getViewPort().getCamera(), ticks);
	}

	public InputManager createInputManager(final int id) {
		InputManager inputManager = new InputManager();
		LWJGLMouse mouse = new LWJGLMouse(getViewPort().getViewShape());
		ViewPort port = getViewPort();
		addNetworkBinding(inputManager, new KeyTrigger(LWJGLKeyboard.instance().getKey('k')), "Backwards",
				"inputbackwards", id);
		addNetworkBinding(inputManager, new KeyTrigger(LWJGLKeyboard.instance().getKey('i')), "Forwards",
				"inputforwards", id);
		addNetworkBinding(inputManager, new KeyTrigger(LWJGLKeyboard.instance().getKey('j')), "Left", "inputleft", id);
		addNetworkBinding(inputManager, new KeyTrigger(LWJGLKeyboard.instance().getKey('l')), "Right", "inputright", id);
		addNetworkBinding(inputManager, new KeyTrigger(LWJGLKeyboard.instance().getKey('t')), "t", "inputT", id);
		addNetworkBinding(inputManager, new MouseWorldXTrigger(mouse, port), "MouseWorldX", "inputmousex", id);
		addNetworkBinding(inputManager, new MouseWorldYTrigger(mouse, port), "MouseWorldY", "inputmousey", id);
		addNetworkBinding(inputManager, new MouseButtonTrigger(mouse.getMouseButton(MouseButton.LEFT_NAME)),
				"LeftMouse", "inputleftmousedown", id);
		addNetworkBinding(inputManager, new MouseButtonTrigger(mouse.getMouseButton(MouseButton.RIGHT_NAME)),
				"RightMouse", "inputrightmousedown", id);
		return inputManager;
	}

	private void addNetworkBinding(InputManager inputManager, InputTrigger trigger, String binding,
			final String command, final int id) {
		inputManager.addBinding(binding, trigger);
		inputManager.addBindingListener(binding, new BindingListener() {
			@Override
			public void onAction(String binding, float value) {
				Message message = new Message(command, new MessageParameter[] { new MessageParameter(id),
						new MessageParameter(value) });

				SpaceshipClient.this.writeMessage(message);
			}
		});
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
		SpaceshipClient client = new SpaceshipClient(new CommandInterpreter(declarations), "playroom-1", Server.PORT);
		client.start();
	}
}
