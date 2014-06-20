package engine.core.execute;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.Socket;

import org.lwjgl.LWJGLException;

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

	private static final int DEFAULT_UPS = 60;
	private int m_ups = DEFAULT_UPS;

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

		Thread t = new Thread(new NativeHandler());
		t.start();

		// listen();
		// startRenderingLoop(m_display);
		startServerHandlerLoop(m_display);
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
	 * Sets the frequency at which update() is called and the MessageBuffer is flushed.
	 * 
	 * @param ups
	 */
	public void setUPS(int ups) {
		m_ups = ups;
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

			m_network.processWriters();

			onServerConnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Display setupDisplay() {
		LWJGLDisplay display = new LWJGLDisplay(1024, 1024, false);
		display.init();

		try {
			org.lwjgl.opengl.Display.releaseContext();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		SingleViewPortLayout layout = new SingleViewPortLayout(display);

		m_camera = new Camera();
		m_camera.setX(0);
		m_camera.setY(0);

		m_viewport = new ViewPort(m_camera);
		layout.setViewPort(m_viewport);

		layout.validate();

		return display;
	}

	private void startServerHandlerLoop(Display display) {
		while (!display.destroyRequested()) {
			Message message = null;
			try {
				message = m_serverReader.readMessage();
			} catch (IOException e) {
				System.err.println("Socket connection closed.");
				System.exit(0);
			}

			if (m_dumpMessages) {
				double timeStamp = System.currentTimeMillis();
				// if (timeStamp - m_lastTimeStamp > 1)
				System.out.println((timeStamp - m_lastTimeStamp) + "---" + message);
				m_lastTimeStamp = timeStamp;
			}

			if (message.getCommand().equals("endtransmission")) {
				m_network.processMessages();
				try {
					synchronized (m_display) {
						org.lwjgl.opengl.Display.makeCurrent();
						render(display);
						org.lwjgl.opengl.Display.releaseContext();
					}
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			} else {
				m_network.bufferMessage(message);
			}
		}
	}

	private void render(Display display) {
		Renderer r = display.getRenderer();

		onRender(r);

		m_viewport.lookThrough(r);
		m_rendering.render(r);

		r.setTransform(new AffineTransform());
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
	 * Called after the Client receives a transmission and renders it. This can be used for rendering a gui.
	 * 
	 * @param renderer
	 */
	public abstract void onRender(Renderer renderer);

	private class NativeHandler implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			long nextGameTick = System.currentTimeMillis();
			int milliSkip = 1000 / m_ups;
			while (true) {
				// System.out.println("Flushing buffer");
				try {
					synchronized (m_display) {
						org.lwjgl.opengl.Display.makeCurrent();
						org.lwjgl.opengl.Display.processMessages();
						org.lwjgl.opengl.Display.releaseContext();
					}
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				m_messageBuffer.flush();
				update(1);
				m_system.update();

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
		}
	}
}