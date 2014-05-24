package engine.gui;

import engine.graphics.Renderer;
import engine.gui.layout.BorderLayout;
import engine.gui.layout.LayoutManager;

public class Panel extends Widget {
	private LayoutManager m_manager = new BorderLayout();
	
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
	
	public void setLayout(LayoutManager manager) {
		m_manager = manager;
	}
	public LayoutManager getLayout() {
		return m_manager;
	}
	
	@Override
	public void validate() {
		super.validate();
		if (m_manager != null) m_manager.layout(this);
	}
	
	@Override
	public void renderWidget(Renderer renderer) {
		for (Widget w : m_children) {
			w.render(renderer);
		}
	}
}
