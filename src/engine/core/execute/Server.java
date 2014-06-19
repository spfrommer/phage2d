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

	private static final int DEFAULT_UPF = 1;
	private int m_upf = DEFAULT_UPF;
	private int m_updatesPassed = 1;

	private int m_clientCount = 0;

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
	 * How many times per second the server should call update
	 * 
	 * @param ups
	 */
	public void setUPS(int ups) {
		m_ups = ups;
	}

	/**
	 * How many updates should the server perform before it transmits the data to the clients - 1 or below means it will
	 * transmit every update
	 * 
	 * @param upf
	 */
	public void setUPF(int upf) {
		m_upf = upf;
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

					Message clientIDMessage = new Message("setclientid", new MessageParameter[] { new MessageParameter(
							m_clientCount) });
					try {
						writer.writeMessage(clientIDMessage);
					} catch (IOException e) {
						e.printStackTrace();
					}

					// Make sure the client has processed its id before we begin syncing
					// the world to it
					/*try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}*/

					onClientConnect(m_clientCount);

					Thread t = new Thread(new ClientHandler(reader, writer));
					t.start();
					m_clientCount++;
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
			if (m_updatesPassed >= m_upf) {
				m_network.processWriters();
				m_network.processMessages();
				boolean transmitted = m_network.trasmitUpdates();
				m_updatesPassed = 1;
				// if (transmitted)
				// System.out.println("Transmitting");
			} else {
				m_updatesPassed++;
			}

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
	 * Gets the Activity used by the server to sync Entities.
	 * 
	 * @return
	 */
	public NetworkSyncActivity getSyncActivity() {
		return m_network;
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

	private class ClientHandler implements Runnable {
		private MessageReader m_reader;
		private MessageWriter m_writer;

		public ClientHandler(MessageReader reader, MessageWriter writer) {
			m_reader = reader;
			m_writer = writer;
		}

		@Override
		public void run() {
			// Syncs world to client
			m_network.bufferAddWriter(m_writer);

			while (true) {
				Message message = null;
				try {
					message = m_reader.readMessage();
				} catch (IOException ex) {
					System.err.println("Socket connection closed.");
					// System.exit(0);
					m_network.bufferRemoveWriter(m_writer);
					return;
				}

				if (m_dumpMessages) {
					double timeStamp = System.currentTimeMillis();
					if (!(message.getCommand().equals("inputmousex") || message.getCommand().equals("inputmousey")))
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
