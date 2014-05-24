package engine.core.implementation.camera.activities;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.implementation.camera.base.Camera;
import engine.inputs.keyboard.Keyboard;

/**
 * Controls a Camera with the keyboard.
 * 
 * @eng.dependencies none
 */
public class KeyboardCameraActivity extends CameraActivity {
	// TODO: InputManager convert
	private Keyboard m_keyboard;
	private MovementProfile m_profile;

	public KeyboardCameraActivity(EntitySystem system, Keyboard keyboard, MovementProfile profile) {
		super(system, new Aspect());
		m_keyboard = keyboard;
		m_profile = profile;
	}

	@Override
	public void control(Camera camera, int ticks) {
		if (m_keyboard.isKeyPressed('W')) {
			camera.incrementY(m_profile.getMoveSpeed() * ticks);
		}
		if (m_keyboard.isKeyPressed('S')) {
			camera.incrementY(-m_profile.getMoveSpeed() * ticks);
		}
		if (m_keyboard.isKeyPressed('A')) {
			camera.incrementX(-m_profile.getMoveSpeed() * ticks);
		}
		if (m_keyboard.isKeyPressed('D')) {
			camera.incrementX(m_profile.getMoveSpeed() * ticks);
		}
		if (m_keyboard.isKeyPressed('E')) {
			camera.incrementZoom(m_profile.getScaleSpeed() * ticks);
		}
		if (m_keyboard.isKeyPressed('Q')) {
			camera.incrementZoom(-m_profile.getScaleSpeed() * ticks);
		}
	}

	@Override
	public void entityAdded(Entity entity) {

	}

	@Override
	public void entityRemoved(Entity entity) {

	}

}
