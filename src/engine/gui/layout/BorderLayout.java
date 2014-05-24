package engine.gui.layout;

import engine.gui.Dimension;
import engine.gui.Widget;

import java.awt.Rectangle;
import java.util.HashMap;

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
	public void removeLayoutWidget(Widget w) {
		m_widgets.remove(m_positions.get(w));
		m_positions.remove(w);
	}
	@Override
	public boolean isManaging(Widget w) {
		return m_widgets.containsKey(w);
	}
	
	@Override
	public void layout(Widget widget) {
		int top = 0;
		int height = top + widget.getSize().getHeight();

		int left = 0;
		int width = left + widget.getSize().getWidth();

		Widget w = null;
		if (m_widgets.containsKey(NORTH)) {
			w = m_widgets.get(NORTH);
			Dimension d = w.getPreferredSize();
			w.setBounds(new Rectangle(left, top, width - left, d.getHeight()));
			top += d.getHeight();
		}
		if (m_widgets.containsKey(SOUTH)) {
			w = m_widgets.get(SOUTH);
			Dimension d = w.getPreferredSize();
			w.setBounds(new Rectangle(left, height - d.getHeight(), width - left, d.getHeight()));
			height -= d.getHeight();
		}
		if (m_widgets.containsKey(EAST)) {
			w = m_widgets.get(EAST);
			Dimension d = w.getPreferredSize();
			w.setBounds(new Rectangle(width - d.getWidth(), top, d.getWidth(), height - top));
			width -= d.getWidth();
		}
		if (m_widgets.containsKey(WEST)) {
			w = m_widgets.get(WEST);
			Dimension d = w.getPreferredSize();
			w.setBounds(new Rectangle(left, top, d.getWidth(), height - top));
			left += d.getWidth();
		}
		if (m_widgets.containsKey(CENTER)) {
			w = m_widgets.get(CENTER);
			//System.out.println("Laying component in center: " + left + ", " + top + " w: " + (width - left) + " h: " + (height - top));
			w.setBounds(new Rectangle(left, top, width - left, height - top));
		}
	}
}
