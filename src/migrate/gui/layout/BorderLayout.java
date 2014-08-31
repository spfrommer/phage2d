package migrate.gui.layout;

import java.util.Collection;
import java.util.HashMap;

import migrate.gui.Dimension;
import migrate.gui.Widget;

public class BorderLayout implements LayoutManager {
	public static final String WEST = "WEST";
	public static final String EAST = "EAST";
	public static final String NORTH = "NORTH";
	public static final String SOUTH = "SOUTH";
	public static final String CENTER = "CENTER";
	
	private HashMap<String, Widget> m_widgets = new HashMap<String, Widget>();
	private HashMap<Widget, String> m_positions = new HashMap<Widget, String>();
	
	@Override
	public void addLayoutWidget(Widget w) {
		addLayoutWidget(w, CENTER);
	}
	
	@Override
	public void addLayoutWidget(Widget w, String value) {
		m_widgets.put(value, w);
		m_positions.put(w, value);
	}
	@Override
	public void addLayoutWidgets(Collection<Widget> widgets) {
		for (Widget w : widgets) {
			addLayoutWidget(w);
		}
	}
	@Override
	public void addLayoutWidgets(Collection<Widget> widgets, String value) {
		for (Widget w : widgets) {
			addLayoutWidget(w, value);
		}		
	}
	@Override
	public void removeLayoutWidget(Widget w) {
		m_widgets.remove(m_positions.get(w));
		m_positions.remove(w);
	}
	@Override
	public void removeLayoutWidgets(Collection<Widget> widgets) {
		for (Widget w : widgets) {
			removeLayoutWidget(w);
		}
	}
	
	@Override
	public void changeLayoutParameter(Widget w, String value) {
		if (m_positions.containsKey(w)) {
			m_widgets.remove(m_positions.get(w));
		}
		m_positions.put(w, value);
		m_widgets.put(value, w);
	}
	
	@Override
	public boolean isManaging(Widget w) {
		return m_widgets.containsKey(w);
	}








	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void layout(Widget widget) {
		int top = 0;
		int height = top + widget.getHeight();

		int left = 0;
		int width = left + widget.getWidth();

		Widget w = null;
		if (m_widgets.containsKey(NORTH)) {
			w = m_widgets.get(NORTH);
			Dimension d = w.getPreferredSize();
			if (d == null) d = new Dimension();
			w.setBounds(left, top, width - left, d.getHeight());
			top += d.getHeight();
		}
		if (m_widgets.containsKey(SOUTH)) {
			w = m_widgets.get(SOUTH);
			Dimension d = w.getPreferredSize();
			if (d == null) d = new Dimension();
			w.setBounds(left, height - d.getHeight(), width - left, d.getHeight());
			height -= d.getHeight();
		}
		if (m_widgets.containsKey(EAST)) {
			w = m_widgets.get(EAST);
			Dimension d = w.getPreferredSize();
			if (d == null) d = new Dimension();
			w.setBounds(width - d.getWidth(), top, d.getWidth(), height - top);
			width -= d.getWidth();
		}
		if (m_widgets.containsKey(WEST)) {
			w = m_widgets.get(WEST);
			Dimension d = w.getPreferredSize();
			if (d == null) d = new Dimension();
			w.setBounds(left, top, d.getWidth(), height - top);
			left += d.getWidth();
		}
		if (m_widgets.containsKey(CENTER)) {
			w = m_widgets.get(CENTER);
			w.setBounds(left, top, width - left, height - top);
		}
	}


}
