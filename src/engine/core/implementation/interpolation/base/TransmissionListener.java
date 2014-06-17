package engine.core.implementation.interpolation.base;

import engine.core.framework.Entity;

/**
 * A listener that fires when a transmission is recieved by the client.
 */
public interface TransmissionListener {
	public void transmissionReceived(Entity receiver);
}