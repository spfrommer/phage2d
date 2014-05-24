package engine.core.implementation.camera.activities;

import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.EntitySystem;
import engine.core.implementation.camera.base.Camera;

/**
 * An abstract Activity that controls a Camera.
 */
public abstract class CameraActivity extends AspectActivity {
	public CameraActivity(EntitySystem system, Aspect aspect) {
		super(system, aspect);
	}

	public abstract void control(Camera camera, int ticks);
}
