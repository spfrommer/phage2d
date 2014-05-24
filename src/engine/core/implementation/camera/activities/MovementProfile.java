package engine.core.implementation.camera.activities;

/**
 * Defines how a KeyboardCameraActivity should move the camera.
 */
public class MovementProfile {
	private double m_moveSpeed;
	private double m_scaleSpeed;

	/**
	 * @param moveSpeed
	 *            move speed in pixels / tick
	 * @param scaleSpeed
	 *            scale amount per tick
	 */
	public MovementProfile(double moveSpeed, double scaleSpeed) {
		m_moveSpeed = moveSpeed;
		m_scaleSpeed = scaleSpeed;
	}

	/**
	 * @return move speed in pixels / tick
	 */
	public double getMoveSpeed() {
		return m_moveSpeed;
	}

	/**
	 * @return scale speed in units / tick
	 */
	public double getScaleSpeed() {
		return m_scaleSpeed;
	}
}
