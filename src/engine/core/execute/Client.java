package engine.core.execute;

import java.awt.geom.AffineTransform;
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

/**
 * An abstract Client that manages networking details and rendering. Extend this class if you want to make a multiplayer
 * game.
 */
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

	private boolean m_dumpMessages = false;
	private double m_lastTimeStamp = System.currentTimeMillis();

	private static final int DEFAULT_FPS = 60;
	private int m_fps = DEFAULT_FPS;

	{
		m_system = new EntitySystem();
		m_rendering = new BasicRenderingActivity(m_system);
	}

	public Client(CommandInterpreter interpreter, String host, int port, DecoderMapper decoder, String images) {
		ImageUtils.initMapping(images);

		PhageSplash splash = new PhageSplash();

		m_interpreter = interpreter;
		Display display = setupDisplay();

		m_network = new NetworkSyncActivity(m_system, decoder);

		m_host = host;
		m_port = port;
		m_display = display;
		splash.setVisible(false);
	}

	/**
	 * Connects to the host, initializes processes, listens for messages, and starts rendering.
	 */
	public void start() {
		connect(m_host, m_port);

		listen();
		startRenderingLoop(m_display);
	}

	/**
	 * Sets the RendingActivity that this Client should use in rendering
	 * 
	 * @param rendering
	 */
	public void setRenderingActivity(RenderingActivity rendering) {
		m_rendering = rendering;
	}

	/**
	 * Whether this Client should dump incoming messages and their timestamp
	 * 
	 * @param dumpMessages
	 */
	public void setDumpMessages(boolean dumpMessages) {
		m_dumpMessages = dumpMessages;
	}

	/**
	 * Sets the rendering FPS.
	 * 
	 * @param fps
	 */
	public void setFPS(int fps) {
		m_fps = fps;
	}

	/**
	 * Establishes a connection and gets the client id
	 */
	private void connect(String host, int port) {
		try {
			m_socket = new Socket(host, port);

			m_serverWriter = new MessageWriter(new ByteWriterInterpreter(new ByteWriter(m_socket.getOutputStream()),
					m_interpreter));
			m_messageBuffer = new MessageBuffer();
			m_messageBuffer.addWriter(m_serverWriter);

			m_network.bufferAddWriter(m_serverWriter);

			m_serverReader = new MessageReader(new ByteReaderInterpreter(new ByteReader(m_socket.getInputStream()),
					m_interpreter));

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

	private void startRenderingLoop(Display display) {
		long nextGameTick = System.currentTimeMillis();
		int milliSkip = 1000 / m_fps;
		while (!display.destroyRequested()) {
			m_messageBuffer.flush();

			m_network.processWriters();
			m_network.processMessages();

			update(1);
			// this call is actually blocking 16 milliseconds
			render(display);

			/*nextGameTick += milliSkip;
			long sleepTime = nextGameTick - System.currentTimeMillis();
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}*/
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

		r.setTransform(new AffineTransform());
		renderGui(r);

		display.getRenderer().setColor(Color.WHITE);
		display.render();
		display.update();
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

	/**
	 * Gets the id assigned to this client by the server
	 * 
	 * @return
	 */
	public int getID() {
		return m_id;
	}

	/**
	 * Gets the Activity used by the client to sync Entities.
	 * 
	 * @return
	 */
	public NetworkSyncActivity getSyncActivity() {
		return m_network;
	}

	/**
	 * Called when the client connects to the server and receives its id
	 */
	protected abstract void onServerConnect();

	/**
	 * Called every update loop
	 * 
	 * @param ticks
	 */
	public abstract void update(int ticks);

	/**
	 * Called so that every subclass can render its specific gui
	 * 
	 * @param renderer
	 */
	public abstract void renderGui(Renderer renderer);

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

				if (m_dumpMessages) {
					double timeStamp = System.currentTimeMillis();
					if (timeStamp - m_lastTimeStamp > 1)
						System.out.println((timeStamp - m_lastTimeStamp) + "---" + message);
					m_lastTimeStamp = timeStamp;
				}

				m_network.bufferMessage(message);
			}
		}
	}
}