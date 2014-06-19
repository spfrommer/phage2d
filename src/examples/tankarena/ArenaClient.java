package examples.tankarena;

import java.util.ArrayList;

import engine.core.execute.Client;
import engine.core.execute.Server;
import engine.core.implementation.camera.activities.CameraActivity;
import engine.core.implementation.camera.activities.ChaseCameraActivity;
import engine.core.implementation.camera.base.ViewPort;
import engine.core.implementation.interpolation.activities.InterpolationActivity;
import engine.core.implementation.interpolation.data.InterpolationData;
import engine.core.implementation.network.base.decoding.BlankDataDecoder;
import engine.core.implementation.network.base.decoding.DecoderMapper;
import engine.core.implementation.network.base.decoding.ErrorDecoder;
import engine.core.implementation.physics.base.ShellDecoder;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.data.PhysicsShellData;
import engine.core.implementation.rendering.data.AnimationData;
import engine.core.implementation.rendering.data.TextureData;
import engine.core.implementation.rendering.data.TextureDecoder;
import engine.core.network.message.Message;
import engine.core.network.message.MessageDeclaration;
import engine.core.network.message.command.CommandInterpreter;
import engine.core.network.message.parameter.MessageParameter;
import engine.graphics.Renderer;
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

/**
 * A Client for the tank arena game. Very similar to that of the Spaceship game.
 */
public class ArenaClient extends Client {
	private CameraActivity m_cam;
	private InterpolationActivity m_interpolation;

	private static DecoderMapper s_decoder;

	static {
		s_decoder = new DecoderMapper();
		s_decoder.addMapping(PhysicsData.class, new ErrorDecoder());
		s_decoder.addMapping(AnimationData.class, new ErrorDecoder());
		s_decoder.addMapping(PhysicsShellData.class, new ShellDecoder());
		s_decoder.addMapping(TextureData.class, new TextureDecoder());
		s_decoder.addMapping(InterpolationData.class, new BlankDataDecoder());
	}

	public ArenaClient(CommandInterpreter interpreter, String server, int port) {
		super(interpreter, server, port, s_decoder, "images-all.txt");
		this.getViewPort().getCamera().setZoom(1);

		// m_cam = new KeyboardCameraActivity(this.getEntitySystem(), LWJGLKeyboard.instance(), new MovementProfile(10,
		// 0.05));
		m_interpolation = new InterpolationActivity(this.getEntitySystem(), this.getSyncActivity());
	}

	@Override
	protected void onServerConnect() {
		createInputManager(this.getID());

		m_cam = new ChaseCameraActivity(this.getEntitySystem(), this.getID());
	}

	@Override
	public void update(int ticks) {
		// m_interpolation.update();

	}

	@Override
	public void onRender(Renderer renderer) {
		m_cam.control(this.getViewPort().getCamera(), 1);
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

				ArenaClient.this.writeMessage(message);
			}
		});
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
		ArenaClient client = new ArenaClient(new CommandInterpreter(declarations), "playroom-1", Server.PORT);
		client.start();
	}
}
