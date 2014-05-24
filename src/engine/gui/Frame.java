package engine.gui;

import java.awt.Rectangle;

import utils.physics.Vector;
import engine.graphics.Renderer;
import engine.gui.border.Border;
import engine.gui.theme.Parameter;
import engine.gui.theme.Theme;
import engine.inputs.mouse.MouseButton;
import engine.inputs.mouse.MouseButtonEvent;
import engine.inputs.mouse.MouseEvent;
import engine.inputs.mouse.MouseExternalEvent;
import engine.inputs.mouse.MouseMovedEvent;

public class Frame extends ThemedWidget {
	public static final String FRAME_THEME = "frame";
	
	private boolean m_dragging;
	private Panel m_panel;
	
	private boolean m_resizing;
	private Vector m_resizeDirection;
	
	public Frame() {
		m_panel = new Panel();
		add(m_panel);
	}
	public Panel getPanel() { return m_panel; }
	
	@Override
	public void validate() {
		super.validate();
		Theme theme = getWidgetTheme();
		if (theme != null) {
			Border border = (Border) ((Parameter) theme.get("border")).getValue();
			getPanel().setBounds(new Rectangle(border.getLeftBorder(), border.getTopBorder(),
					getSize().getWidth() - (border.getRightBorder() + border.getLeftBorder()),
					getSize().getHeight() - (border.getTopBorder() + border.getBottomBorder())));
		}
	}
			
	
	@Override
	public String getThemeName() {
		return "resizableframe";
	}

	@Override
	public void onMouseEvent(MouseEvent m) {
		super.onMouseEvent(m);
		
	}
	
	@Override
	public void renderWidget(Renderer renderer) {
		super.renderWidget(renderer);

		Border border = (Border) ((Parameter) getWidgetTheme().get("border")).getValue();


		Rectangle panelRect = new Rectangle(border.getLeftBorder(), border.getTopBorder(),
				getSize().getWidth() - (border.getRightBorder() + border.getLeftBorder()),
				getSize().getHeight() - (border.getTopBorder() + border.getBottomBorder()));

		renderer.setClip(panelRect);

		m_panel.render(renderer);

		renderer.setClip(null);
	}
}
