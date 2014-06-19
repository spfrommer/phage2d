package engine.core.implementation.network.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import utils.TwoWayHashMap;
import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.interpolation.base.TransmissionListener;
import engine.core.implementation.interpolation.base.TransmissionReceiver;
import engine.core.implementation.network.base.decoding.DecoderMapper;
import engine.core.implementation.network.base.decoding.EntityDecoder;
import engine.core.implementation.network.data.NetworkData;
import engine.core.implementation.network.logic.NetworkSyncLogic;
import engine.core.network.lowlevel.MessageWriter;
import engine.core.network.message.Message;
import engine.core.network.message.parameter.MessageParameter;

/**
 * Syncs Entities across a network
 * 
 * @eng.dependencies NetworkSyncLogic, NetworkData
 */
public class NetworkSyncActivity extends AspectActivity implements TransmissionReceiver {
	/**
	 * The MessageWriters that should be managed by this Activity
	 */
	private List<MessageWriter> m_writers;

	/**
	 * Buffered ComponentType for fetching Components
	 */
	private ComponentType m_syncType;
	/**
	 * Buffered ComponentType for fetching Components
	 */
	private ComponentType m_dataType;

	/**
	 * Maps Entities to their NetworkIDs
	 */
	private TwoWayHashMap<Entity, Integer> m_idMapper;

	/**
	 * The current id - for assigning new ids to entities
	 */
	private int currentID = 0;

	/**
	 * buffers the received messages until the next update loop
	 */
	private List<Message> m_messageBuffer;

	/**
	 * Makes sure that the receiving thread can't add objects while the buffer is being analyzed
	 */
	private Lock m_messageBufferLock;

	/**
	 * Writers to add in the next update loop
	 */
	private List<MessageWriter> m_writerAddBuffer;

	/**
	 * Writers to remove in the next update loop
	 */
	private List<MessageWriter> m_writerRemoveBuffer;

	/**
	 * Makes sure that m_writers is being modified while writers are being added or removed
	 */
	private Lock m_writerModifyLock;

	/**
	 * Defines how to decode an entity
	 */
	private DecoderMapper m_decoder;

	/**
	 * A list of listeners which are fired when the NetworkSyncActivity processes the message buffer.
	 */
	private List<TransmissionListener> m_transmissionListeners;

	{
		m_messageBuffer = new ArrayList<Message>();
		m_messageBufferLock = new ReentrantLock();

		m_writerAddBuffer = new ArrayList<MessageWriter>();
		m_writerRemoveBuffer = new ArrayList<MessageWriter>();
		m_writerModifyLock = new ReentrantLock();

		m_writers = new ArrayList<MessageWriter>();

		m_idMapper = new TwoWayHashMap<Entity, Integer>();

		m_transmissionListeners = new ArrayList<TransmissionListener>();

		m_syncType = TypeManager.getType(NetworkSyncLogic.class);
		m_dataType = TypeManager.getType(NetworkData.class);
	}

	/**
	 * Constructs a new NetworkSyncActivity
	 * 
	 * @param system
	 *            the System to act on
	 * @param decoder
	 *            how the Activity should decode incoming entities
	 * @param server
	 *            whether or not this Sync activity should act as a server or client
	 */
	public NetworkSyncActivity(EntitySystem system, DecoderMapper decoder) {
		super(system, new Aspect(TypeManager.getType(NetworkSyncLogic.class), TypeManager.getType(NetworkData.class)));
		m_decoder = decoder;
	}

	@Override
	public void entityAdded(Entity entity) {
		NetworkData network = (NetworkData) entity.getComponent(m_dataType);
		if (network.id == -1)
			network.id = currentID++;

		m_idMapper.put(entity, network.id);

		if (network.sync) {
			NetworkSyncLogic component = (NetworkSyncLogic) entity.getComponent(m_syncType);
			for (MessageWriter writer : m_writers) {
				try {
					writer.writeMessage(component.getCreationMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void entityRemoved(Entity entity) {
		NetworkData network = (NetworkData) entity.getComponent(m_dataType);
		if (network.sync) {
			NetworkSyncLogic component = (NetworkSyncLogic) entity.getComponent(m_syncType);
			for (MessageWriter writer : m_writers) {
				try {
					writer.writeMessage(component.getRemovalMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void addTransmissionListener(TransmissionListener listener) {
		m_transmissionListeners.add(listener);
	}

	@Override
	public void removeTransmissionListener(TransmissionListener listener) {
		m_transmissionListeners.remove(listener);
	}

	/**
	 * Adds and removes MessageWriters
	 */
	public void processWriters() {
		try {
			m_writerModifyLock.lock();
			for (MessageWriter writer : m_writerAddBuffer) {
				for (Entity entity : m_idMapper.getKeys()) {
					NetworkData data = (NetworkData) entity.getComponent(m_dataType);
					if (data.sync) {
						NetworkSyncLogic sync = (NetworkSyncLogic) entity.getComponent(m_syncType);
						try {
							writer.writeMessage(sync.getCreationMessage());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				m_writers.add(writer);
			}

			for (MessageWriter writer : m_writerRemoveBuffer) {
				m_writers.remove(writer);
			}
			m_writerAddBuffer.clear();
			m_writerRemoveBuffer.clear();
		} finally {
			m_writerModifyLock.unlock();
		}
	}

	/**
	 * Processes all the Messages in the message buffer, then clears it
	 */
	public void processMessages() {
		try {
			m_messageBufferLock.lock();

			Set<Entity> updatedEntities = new HashSet<Entity>();

			for (Message message : m_messageBuffer) {
				if (message == null)
					System.err.println("Message " + message + " equals null. (nsp.handleMessages) ");

				if (message.getCommand() == null)
					System.err.println("Message " + message + " command equals null. (nsp.handleMessages) ");

				if (message.getCommand().equals("update")) {
					Entity receiver = m_idMapper.getBackward((message.getParameters()[0]).getIntValue());

					if (receiver == null)
						System.err.println("Null receiver in NetworkSyncProcess.handleMessages(): " + message);

					NetworkSyncLogic syncLogic = (NetworkSyncLogic) receiver.getComponent(TypeManager
							.getType(NetworkSyncLogic.class));
					syncLogic.processMessage(message);

					updatedEntities.add(receiver);
				} else if (message.getCommand().equals("addentity")) {
					Entity entity = EntityDecoder.decode(message.getParameters()[0].getStringValue(), m_decoder);
					this.getSystem().addEntity(entity);
				} else if (message.getCommand().equals("removeentity")) {
					Entity receiver = m_idMapper.getBackward((message.getParameters()[0]).getIntValue());
					this.getSystem().removeEntity(receiver);
				}
			}

			for (Entity e : updatedEntities) {
				for (TransmissionListener listener : m_transmissionListeners)
					listener.transmissionReceived(e);
			}

			m_messageBuffer.clear();
		} finally {
			m_messageBufferLock.unlock();
		}
	}

	/**
	 * Transmits all updates that Components want the other side to know.
	 * 
	 * @return if any data was transmitted
	 */
	public boolean trasmitUpdates() {
		List<Entity> entities = this.getEntities();

		boolean transmitted = false;
		// concurrent modification exception?
		for (Entity entity : entities) {
			NetworkSyncLogic component = (NetworkSyncLogic) entity.getComponent(m_syncType);
			ArrayList<Message> updateMessages = component.getUpdateMessages();
			for (Message updateMessage : updateMessages) {
				for (MessageWriter writer : m_writers) {
					try {
						writer.writeMessage(updateMessage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				transmitted = true;
			}
		}

		if (transmitted) {
			for (MessageWriter writer : m_writers) {
				try {
					writer.writeMessage(new Message("endtransmission", new MessageParameter[0]));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return transmitted;
	}

	/**
	 * Buffers a writer to be added
	 * 
	 * @param writer
	 */
	public void bufferAddWriter(MessageWriter writer) {
		try {
			m_writerModifyLock.lock();
			m_writerAddBuffer.add(writer);
		} finally {
			m_writerModifyLock.unlock();
		}
	}

	/**
	 * Buffers a writer to be removed
	 * 
	 * @param writer
	 */
	public void bufferRemoveWriter(MessageWriter writer) {
		try {
			m_writerModifyLock.lock();
			m_writerRemoveBuffer.add(writer);
		} finally {
			m_writerModifyLock.unlock();
		}
	}

	/**
	 * Adds a Message that this Activity should handle
	 * 
	 * @param message
	 */
	public void bufferMessage(Message message) {
		try {
			m_messageBufferLock.lock();
			m_messageBuffer.add(message);
		} finally {
			m_messageBufferLock.unlock();
		}
	}
}
