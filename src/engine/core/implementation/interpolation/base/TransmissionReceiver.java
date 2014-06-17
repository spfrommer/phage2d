package engine.core.implementation.interpolation.base;

/**
 * An interface used in interpolation that allows a TransmissionListener to listen for updates.
 */
public interface TransmissionReceiver {
	public void addTransmissionListener(TransmissionListener listener);

	public void removeTransmissionListener(TransmissionListener listener);
}
