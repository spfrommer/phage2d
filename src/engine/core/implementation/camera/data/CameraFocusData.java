package engine.core.implementation.camera.data;

import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

/**
 * Tells the camera of the client what it should focus on
 */
public class CameraFocusData extends DataComponent {
	public int cameraID;

	public CameraFocusData() {
		super();
	}

	@Override
	public Component copy() {
		CameraFocusData focus = new CameraFocusData();
		focus.cameraID = cameraID;
		return focus;
	}

}
