package migrate.gui.layout;

import java.util.Collection;

import migrate.gui.Widget;

public interface LayoutManager {
	public void addLayoutWidget(Widget w);
	public void addLayoutWidgets(Collection<Widget> widgets);
	public void addLayoutWidget(Widget w, String value);
	public void addLayoutWidgets(Collection<Widget> widgets, String value);
	public void removeLayoutWidget(Widget w);
	public void removeLayoutWidgets(Collection<Widget> w);
	
	public void changeLayoutParameter(Widget w, String value);
	
	public boolean isManaging(Widget w);
	
	//Removeall
	public void clear();
	
	//Will layout the components for the specified widget
	public void layout(Widget w);
}
