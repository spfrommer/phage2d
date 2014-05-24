package engine.core.implementation.camera.base;


/**
 * This class assumes that there is no other renderer on this display, as this arranges its viewport to use up the
 * entire display
 * 
 * @author Daniel Pfrommer
 */
public class SingleViewPortLayout implements ResizeListener {
	private Display m_display;
	private ViewPort m_viewport;

	public SingleViewPortLayout(Display display) {
		m_display = display;
		// Add this to the render loop
		// display.addRenderable(this);
		// Add resize listener
		display.addResizedListener(this);
	}

	public void setViewPort(ViewPort port) {
		m_viewport = port;
	}

	public ViewPort getViewPort() {
		return m_viewport;
	}

	@Override
	public void resized(int width, int height) {
		// Reorganized viewports
		validate();
	}

	/**
	 * Rearranges the viewports based on layout "updates" the layout
	 */
	public void validate() {
		if (m_viewport != null) {
			m_viewport.setLocation(0, 0);
			m_viewport.resized(m_display.getWidth(), m_display.getHeight());
		}
	}

	/*@Override
	public void render(Renderer r) {
		m_viewport.render(r);
	}*/
}
