package engine.gui.layout;

import engine.gui.Widget;

public interface LayoutManager {
	public void addLayoutWidget(Widget w);
	public void addLayoutWidget(Widget w, String value);
	public void removeLayoutWidget(Widget w);
	public boolean isManaging(Widget w);
	
	//Will layout the components for the specified widget
	public void layout(Widget w);
}
