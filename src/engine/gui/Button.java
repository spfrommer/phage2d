package engine.gui;

import engine.graphics.Renderer;
import engine.gui.theme.Area;
import engine.inputs.mouse.MouseButton;
import engine.inputs.mouse.MouseButtonEvent;
import engine.inputs.mouse.MouseEvent;
import engine.inputs.mouse.MouseExternalEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Button extends ThemedWidget {
	private static final Logger logger = LoggerFactory.getLogger(Button.class);
	
	public static final String BUTTON_IMAGE_PROPERTY = "button.normal";
	public static final String PRESSED_BUTTON_IMAGE_PROPERTY = "button.pressed";
	
	private ArrayList<ActionListener> m_listeners = new ArrayList<ActionListener>();
	private Widget m_widget;
	
	public Button() {
		//Make sure animation state is properly initialized
		getAnimationState().setState("pressed", false);
	}
	public Button(Widget w) {
		this();
		setWidget(w);
	}
	
	public void setWidget(Widget w) { m_widget = w; }
	public Widget getWidget() { return m_widget; }
	public boolean isDown() { return getAnimationState().getState("pressed"); }
	public void setDown(boolean down) { getAnimationState().setState("pressed", down); }

	public void addActionListener(ActionListener listener) {
		m_listeners.add(listener);
	}
	public void removeActionListener(ActionListener listener) {
		m_listeners.remove(listener);
	}
	
	@Override
	public void onMouseEvent(MouseEvent e) {
		super.onMouseEvent(e);
		if (e instanceof MouseExternalEvent && ((MouseExternalEvent) e).getEvent() instanceof MouseButtonEvent) {
			MouseButtonEvent be = (MouseButtonEvent) ((MouseExternalEvent) e).getEvent();
			if (be.getButton().getName().equals(MouseButton.LEFT_NAME) && isDown()) {
				setDown(false);
			}
		}
		if (e instanceof MouseButtonEvent) {
			MouseButtonEvent be = (MouseButtonEvent) e;
			if (be.getButton().getName().equals(MouseButton.LEFT_NAME)) {
				if (be.isDown()) {
					setDown(true);
					for (ActionListener listener : m_listeners) {
						listener.actionPerformed(new ActionEvent(be, 0, "pressed"));
					}
				} else {
					setDown(false);
				}
			}
		}
	}
	
	@Override
	public String getThemeName() {
		return "button";
	}
	
	@Override
	public void renderWidget(Renderer renderer) {
		super.renderWidget(renderer);
		//Now take into account the button size
		int centerX = getSize().getWidth()/2;
		int centerY = getSize().getHeight()/2;
		
		//Now take into account the widget size
		if (m_widget != null) {
			centerX -= m_widget.getPreferredSize().getWidth()/2;
			centerY -= m_widget.getPreferredSize().getHeight()/2;
		}
		//We don't need to push and pop as that will be handled by render()
		renderer.translate(centerX, centerY);
		if (m_widget != null) m_widget.render(renderer);
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension theme = super.getPreferredSize();
		if (m_widget != null) {
			theme.setHeight(theme.getHeight() + m_widget.getPreferredSize().getHeight());
			theme.setWidth(theme.getWidth() + m_widget.getPreferredSize().getWidth());
		}
		logger.debug("Min size: " + theme);
		return theme;
	}
}
