package engine.core.execute;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import utils.image.ImageUtils;
import engine.core.framework.EntitySystem;
import engine.core.implementation.network.activities.NetworkSyncActivity;
import engine.core.implementation.network.base.decoding.DecoderMapper;
import engine.core.network.NetworkInputHub;
import engine.core.network.lowlevel.ByteReader;
import engine.core.network.lowlevel.ByteReaderInterpreter;
import engine.core.network.lowlevel.ByteWriter;
import engine.core.network.lowlevel.ByteWriterInterpreter;
import engine.core.network.lowlevel.MessageReader;
import engine.core.network.lowlevel.MessageWriter;
import engine.core.network.message.Message;
import engine.core.network.message.command.CommandInterpreter;
import engine.core.network.message.parameter.MessageParameter;

/**
 * An abstract Server that manages networking details. Extend this class if you want to make a multiplayer game.
 */
public abstract class Server {
	private static NetworkInputHub m_inputHub = new NetworkInputHub();
	private EntitySystem m_system;
	private CommandInterpreter m_interpreter;
	private NetworkSyncActivity m_network;

	private int m_port;
	public static final int PORT = 5000;

	private boolean m_dumpMessages = false;
	private double m_lastTimeStamp = System.currentTimeMillis();

	private static final int DEFAULT_UPS = 30;
	private int m_ups = DEFAULT_UPS;

	{
		m_system = new EntitySystem();
	}

	public Server(CommandInterpreter interpreter, final int port, DecoderMapper decoder, String images) {
		ImageUtils.initMapping(images);
		m_interpreter = interpreter;

		m_network = new NetworkSyncActivity(this.getEntitySystem(), decoder);
		m_port = port;
	}

	/**
	 * Starts listening and game loop.
	 */
	public void start() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				listen(m_port);
			}
		});
		t.start();

		onStart();

		startGameLoop();

		onStop();
	}

	/**
	 * Whether this Server should print all incoming messages + the time difference
	 * 
	 * @param dumpMessages
	 */
	public void setDumpMessages(boolean dumpMessages) {
		m_dumpMessages = dumpMessages;
	}

	/**
	 * How many times per second the server should send update messages to the clients
	 * 
	 * @param ups
	 */
	public void setUPS(int ups) {
		m_ups = ups;
	}

	private void listen(int port) {
		try {
			ServerSocket s = new ServerSocket(port);
			try {
				while (true) {
					Socket clientSocket = s.accept();
					MessageWriter writer = new MessageWriter(new ByteWriterInterpreter(new ByteWriter(
							clientSocket.getOutputStream()), m_interpreter));
					MessageReader reader = new MessageReader(new ByteReaderInterpreter(new ByteReader(
							clientSocket.getInputStream()), m_interpreter));
					Thread t = new Thread(new ClientHandler(reader, writer));
					t.start();
				}
			} finally {
				s.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void startGameLoop() {
		long nextGameTick = System.currentTimeMillis();
		int milliSkip = 1000 / m_ups;
		while (true) {
			update(1);
			m_network.processWriters();
			m_network.processMessages();
			m_network.trasmitUpdates();

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

	protected EntitySystem getEntitySystem() {
		return m_system;
	}

	protected NetworkInputHub getInputHub() {
		return m_inputHub;
	}

	/**
	 * Called after listen() but before startGameLoop()
	 */
	public abstract void onStart();

	/**
	 * Called after startGameLoop() exits
	 */
	public abstract void onStop();

	/**
	 * Called when a client connects and knows its id
	 * 
	 * @param clientID
	 */
	public abstract void onClientConnect(int clientID);

	/**
	 * Called every update loop
	 * 
	 * @param ticks
	 */
	public abstract void update(int ticks);

	private static int s_clientCount = 0;

	private class ClientHandler implements Runnable {
		private MessageReader m_reader;
		private MessageWriter m_writer;
		private int m_clientID;

		public ClientHandler(MessageReader reader, MessageWriter writer) {
			m_reader = reader;
			m_writer = writer;

			m_clientID = s_clientCount++;
		}

		@Override
		public void run() {
			Message clientIDMessage = new Message("setclientid", new MessageParameter[] { new MessageParameter(
					m_clientID) });
			try {
				m_writer.writeMessage(clientIDMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Make sure the client has processed its id before we begin syncing
			// the world to it
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			onClientConnect(m_clientID);

			// Syncs world to client
			m_network.bufferAddWriter(m_writer);

			while (true) {
				Message message = null;
				try {
					message = m_reader.readMessage();
				} catch (IOException ex) {
					System.err.println("Socket connection closed.");
					System.exit(0);
				}

				if (m_dumpMessages) {
					double timeStamp = System.currentTimeMillis();
					if (timeStamp - m_lastTimeStamp > 1)
						System.out.println((timeStamp - m_lastTimeStamp) + "---" + message);
					m_lastTimeStamp = timeStamp;
				}

				if (message.getCommand().contains("input")) {
					m_inputHub.broadcast(message);
				} else {
					if (message.getCommand().equals("socketshutdown")) {
						m_network.bufferRemoveWriter(m_writer);
						return;
					} else {
						m_network.bufferMessage(message);
					}
				}
			}
		}
	}
}
