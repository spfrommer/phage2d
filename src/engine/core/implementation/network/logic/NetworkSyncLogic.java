package engine.core.implementation.network.logic;

import java.util.ArrayList;

import engine.core.framework.Aspect;
import engine.core.framework.component.LogicComponent;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.network.base.communication.message.Message;
import engine.core.implementation.network.data.NetworkData;

/**
 * An abstract component to sync an entity over a network
 */
public abstract class NetworkSyncLogic extends LogicComponent {
	private NetworkData m_idComponent;

	public NetworkSyncLogic(Aspect dependencies) {
		super(dependencies.addType(TypeManager.typeOf(NetworkData.class)));
	}

	/**
	 * Returns the network id of this Entity, retrieved from the NetworkData component
	 * 
	 * @return
	 */
	public int getID() {
		return m_idComponent.id;
	}

	/**
	 * Gets the creation Message for this Entity: "addentity" + parameters
	 * 
	 * @return
	 */
	public abstract Message getCreationMessage();

	/**
	 * Gets the removal Message for this Entity: "removeentity" + parameters
	 * 
	 * @return
	 */
	public abstract Message getRemovalMessage();

	/**
	 * Gets all the update Messages for this Entity: "update" + parameters
	 * 
	 * @return
	 */
	public abstract ArrayList<Message> getUpdateMessages();

	/**
	 * Processes an update Message
	 * 
	 * @param update
	 */
	public abstract void processMessage(Message update);

	@Override
	public void loadDependencies() {
		m_idComponent = (NetworkData) this.loadDependency(TypeManager.typeOf(NetworkData.class));
	}
}