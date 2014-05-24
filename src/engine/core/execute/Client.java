package engine.core.execute;

import java.io.IOException;
import java.net.Socket;

import utils.image.ImageUtils;
import engine.core.framework.EntitySystem;
import engine.core.implementation.camera.base.Camera;
import engine.core.implementation.camera.base.Display;
import engine.core.implementation.camera.base.SingleViewPortLayout;
import engine.core.implementation.camera.base.ViewPort;
import engine.core.implementation.network.activities.NetworkSyncActivity;
import engine.core.implementation.network.base.decoding.DecoderMapper;
import engine.core.implementation.rendering.activities.BasicRenderingActivity;
import engine.core.implementation.rendering.activities.RenderingActivity;
import engine.core.network.MessageBuffer;
import engine.core.network.lowlevel.ByteReader;
import engine.core.network.lowlevel.ByteReaderInterpreter;
import engine.core.network.lowlevel.ByteWriter;
import engine.core.network.lowlevel.ByteWriterInterpreter;
import engine.core.network.lowlevel.MessageReader;
import engine.core.network.lowlevel.MessageWriter;
import engine.core.network.message.Message;
import engine.core.network.message.command.CommandInterpreter;
import engine.graphics.Color;
import engine.graphics.Renderer;
import engine.graphics.lwjgl.LWJGLDisplay;

public abstract class Client {
	private Socket m_socket;
	private MessageBuffer m_messageBuffer;
	private MessageWriter m_serverWriter;
	private MessageReader m_serverReader;
	private EntitySystem m_system;
	private CommandInterpreter m_interpreter;
	private NetworkSyncActivity m_network;
	private RenderingActivity m_rendering;

	private ViewPort m_viewport;
	private Camera m_camera;

	private int m_id;

	private String m_host;
	private int m_port;
	private Display m_display;

	private static final boolean DUMP_MESSAGES = false;

	public Client(CommandInterpreter interpreter, String host, int port,
			DecoderMapper decoder) {
		ImageUtils.initMapping();

		PhageSplash splash = new PhageSplash();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		m_interpreter = interpreter;
		Display display = setupDisplay();

		m_system = new EntitySystem();
		m_network = new NetworkSyncActivity(m_system, decoder);
		m_rendering = new BasicRenderingActivity(m_system);

		m_host = host;
		m_port = port;
		m_display = display;
		splash.setVisible(false);
	}

	public void start() {
		connect(m_host, m_port);

		initProcesses();

		listen();
		startRenderingLoop(m_display);
	}

	public void setRenderingActivity(RenderingActivity rendering) {
		m_rendering = rendering;
	}

	/**
	 * Establishes a connection and gets the client id
	 */
	private void connect(String host, int port) {
		try {
			m_socket = new Socket(host, port);

			m_serverWriter = new MessageWriter(new ByteWriterInterpreter(
					new ByteWriter(m_socket.getOutputStream()), m_interpreter));
			m_messageBuffer = new MessageBuffer();
			m_messageBuffer.addWriter(m_serverWriter);

			m_network.bufferAddWriter(m_serverWriter);

			m_serverReader = new MessageReader(new ByteReaderInterpreter(
					new ByteReader(m_socket.getInputStream()), m_interpreter));

			Message idMessage = m_serverReader.readMessage();
			m_id = idMessage.getParameters()[0].getIntValue();

			onServerConnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Listens for server messages
	 */
	private void listen() {
		Thread t = new Thread(new ServerReader());
		t.start();
	}

	private Display setupDisplay() {
		LWJGLDisplay display = new LWJGLDisplay(1024, 1024);
		display.init();

		SingleViewPortLayout layout = new SingleViewPortLayout(display);

		m_camera = new Camera();
		m_camera.setX(0);
		m_camera.setY(0);

		m_viewport = new ViewPort(m_camera);
		layout.setViewPort(m_viewport);

		layout.validate();

		return display;
	}

	private final int FPS = 60;

	private void startRenderingLoop(Display display) {
		long nextGameTick = System.currentTimeMillis();
		int milliSkip = 1000 / FPS;
		while (!display.destroyRequested()) {
			m_messageBuffer.flush();

			m_network.processWriters();
			m_network.processMessages();

			updateProcesses(1);

			render(display);

			nextGameTick += milliSkip;
			long sleepTime = nextGameTick - System.currentTimeMillis();
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			m_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private void render(Display display) {
		Renderer r = display.getRenderer();

		m_viewport.lookThrough(r);
		m_rendering.render(r);

		display.getRenderer().setColor(Color.WHITE);
		display.render();
		display.update(FPS);
	}

	protected void writeMessage(Message message) {
		m_messageBuffer.bufferMessage(message);
	}

	protected ViewPort getViewPort() {
		return m_viewport;
	}

	protected EntitySystem getEntitySystem() {
		return m_system;
	}

	public int getID() {
		return m_id;
	}

	protected abstract void onServerConnect();

	public abstract void initProcesses();

	public abstract void updateProcesses(int ticks);

	private class ServerReader implements Runnable {
		public ServerReader() {
		}

		@Override
		public void run() {
			while (true) {
				Message message = null;
				try {
					message = m_serverReader.readMessage();
				} catch (IOException e) {
					System.err.println("Socket connection closed.");
					System.exit(0);
				}
				if (DUMP_MESSAGES)
					System.out.println(message);
				m_network.bufferMessage(message);
			}
		}
	}
}