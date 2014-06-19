package engine.core.implementation.network.data;

import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

/**
 * Contains the network id of the Entity and whether it should be synced.
 */
public class NetworkData extends DataComponent {
	/**
	 * The network id of the Entity
	 */
	public int id;
	/**
	 * Whether this Entity should be synced over the network
	 */
	public boolean sync;

	{
		id = -1;
		sync = true;
	}

	public NetworkData() {
		super();
	}

	@Override
	public Component copy() {
		NetworkData network = new NetworkData();
		network.id = this.id;
		network.sync = this.sync;
		return network;
	}
}
