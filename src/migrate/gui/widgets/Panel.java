package migrate.gui.widgets;

import java.awt.Rectangle;

import migrate.gui.ContainerWidget;
import migrate.gui.FocusPolicy.ClickFocusPolicy;
import migrate.gui.Widget;
import migrate.gui.layout.LayoutManager;
import migrate.input.Key;
import migrate.input.Keyboard;
import engine.graphics.Renderer;

public class Panel extends ContainerWidget {
	//private static final Logger logger = LoggerFactory.getLogger(Panel.class);

	private LayoutManager m_manager = null;
	
	public Panel() {
		setFocusPolicy(new ClickFocusPolicy());
	}
	
	@Override
	public void add(Widget w) {
		super.add(w);
		if (m_manager != null) m_manager.addLayoutWidget(w);
	}
	
	public void add(Widget w, String manager) {
		super.add(w);
		if (m_manager != null) m_manager.addLayoutWidget(w, manager);
	}
	
	@Override
	public void remove(Widget w) {
		super.remove(w);
		//Remove it from the manager if necessary
		if (m_manager != null && m_manager.isManaging(w)) m_manager.removeLayoutWidget(w);
	}
	
	public void changeLayoutParameter(Widget w, String value) {
		m_manager.changeLayoutParameter(w, value);
	}
	
	public void setLayout(LayoutManager manager) {
		if (m_manager != null) m_manager.clear();
		m_manager = manager;
		if (getChildren().size() > 0) 
			m_manager.addLayoutWidgets(getChildren());
	}
	public LayoutManager getLayoutManager() {
		return m_manager;
	}
	
	/**
	 * Overriden so that the panel will properly layout its children
	 */
	@Override
	public void validate() {
		if (m_manager != null) m_manager.layout(this);
		for (Widget w : getChildren()) w.validate();
	}
	
	@Override
	public void renderWidget(Renderer r) {
		r.setClip(new Rectangle(0, 0, getWidth(), getHeight()));
		//r.fillRect(0, 0, getWidth(), getHeight());
		for (Widget w : getChildren()) {
			w.render(r);
		}
		r.setClip(null);
	}
	@Override
	public void keyPressed(Keyboard keyboard, Key key) {
		super.keyPressed(keyboard, key);
	}
}
