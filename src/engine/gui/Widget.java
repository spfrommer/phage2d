package engine.gui;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import utils.physics.Vector;
import engine.graphics.Renderer;
import engine.gui.theme.Theme;
import engine.inputs.InputEvent;
import engine.inputs.keyboard.KeyEvent;
import engine.inputs.keyboard.Keyboard;
import engine.inputs.mouse.Mouse;
import engine.inputs.mouse.MouseClickedEvent;
import engine.inputs.mouse.MouseEnteredEvent;
import engine.inputs.mouse.MouseEvent;
import engine.inputs.mouse.MouseExitedEvent;
import engine.inputs.mouse.MouseExternalEvent;

public abstract class Widget implements Focusable {
	private int m_x;
	private int m_y;
	protected Mouse m_mouse;
	protected Keyboard m_keyboard;
	private Theme m_theme;
	private Dimension m_size = new Dimension();
	private Dimension m_preferredSize;
	private Dimension m_maxSize = new Dimension();
	private Dimension m_minimumSize = new Dimension();

	protected ArrayList<Widget> m_children = new ArrayList<Widget>();
	private Widget m_parent;
	protected boolean m_mousedOver;
	private FocusManager m_focusManager;
	private AnimationState m_animationState = new AnimationState();

	public Dimension getSize() {
		return m_size;
	}

	public Dimension getPreferredSize() {
		if (m_preferredSize != null)
			return m_preferredSize;
		else
			return getMinimumSize();
	}

	public Dimension getMaxSize() {
		return m_maxSize;
	}

	public Dimension getMinimumSize() {
		return m_minimumSize;
	}

	public int getX() {
		return m_x;
	}

	public int getY() {
		return m_y;
	}

	public boolean isMousedOver() {
		return getAnimationState().getState("hover");
	}

	public AnimationState getAnimationState() {
		return m_animationState;
	}

	public void setX(int x) {
		m_x = x;
	}

	public void setY(int y) {
		m_y = y;
	}

	public void setSize(Dimension d) {
		m_size = d;
		validate();
	}

	public void setPreferredSize(Dimension d) {
		m_preferredSize = d;
	}

	public void setMaxSize(Dimension d) {
		m_maxSize = d;
	}

	public void setMinimumSize(Dimension d) {
		m_minimumSize = d;
	}

	public void setMousedOver(boolean mouse) {
		getAnimationState().setState("hover", mouse);
	}

	public Widget() {
		setFocused(false);
	}

	public void setParent(Widget w) {
		if (m_parent != null)
			m_parent.remove(this);
		m_parent = w;
	}

	public Widget getParent() {
		return m_parent;
	}

	public void setMouse(Mouse mouse) {
		m_mouse = mouse;
		for (Widget w : getChildren()) {
			w.setMouse(mouse);
		}
	}

	public void setKeyboard(Keyboard keyboard) {
		m_keyboard = keyboard;
		for (Widget w : getChildren()) {
			w.setKeyboard(keyboard);
		}
	}

	public Mouse getMouse() {
		return m_mouse;
	}

	public Keyboard getKeyboard() {
		return m_keyboard;
	}

	@Override
	public void setFocused(boolean focus) {
		getAnimationState().setState("keyboardFocus", focus);
	}

	public boolean hasFocus() {
		return getAnimationState().getState("keyboardFocus");
	}

	public void setFocusManager(FocusManager manager) {
		m_focusManager = manager;
		for (Widget widget : getChildren()) {
			widget.setFocusManager(manager);
		}
	}

	public Theme getTheme() {
		return m_theme;
	}

	public void setTheme(Theme theme) {
		m_theme = theme;
		for (Widget w : getChildren()) {
			w.setTheme(theme);
		}
	}

	public FocusManager getFocusManager() {
		return m_focusManager;
	}

	public void requestFocus() {
		m_focusManager.requestFocus(this);// Will get the focus
	}

	public void add(Widget w) {
		m_children.add(w);
		w.setParent(this);
		if (w.getTheme() == null)
			w.setTheme(getTheme());
	}

	public void remove(Widget w) {
		m_children.remove(w);
		w.setParent(null);
	}

	public AffineTransform getAbsoluteTransformation() {
		AffineTransform transform;
		if (m_parent != null) {
			transform = m_parent.getAbsoluteTransformation();
		} else {
			transform = new AffineTransform();
		}
		// Apply the x/y
		transform.concatenate(getLocalTransformation());
		return transform;
	}

	public AffineTransform getLocalTransformation() {
		return AffineTransform.getTranslateInstance(getX(), getY());
	}

	public Rectangle getAbsoluteBounds() {
		AffineTransform absTrans = getAbsoluteTransformation();
		Rectangle rect = new Rectangle((int) absTrans.getTranslateX(), (int) absTrans.getTranslateY(),
				(int) getBounds().getWidth(), (int) getBounds().getHeight());
		return rect;
	}

	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), getSize().getWidth(), getSize().getHeight());
	}

	public void setBounds(Rectangle rect) {
		setX((int) rect.getX());
		setY((int) rect.getY());
		setSize(new Dimension((int) rect.getWidth(), (int) rect.getHeight()));
	}

	public ArrayList<Widget> getChildren() {
		return m_children;
	}

	public void onInputEvent(InputEvent e) {
		// Notify all children of the event
		if (e instanceof KeyEvent && hasFocus()) {
			onKeyboardEvent((KeyEvent) e);
		} else if (e instanceof MouseEvent) {
			MouseEvent m = (MouseEvent) e;
			Rectangle absolute = getAbsoluteBounds();

			Vector localPos = new Vector(m.getCoordinates().subtract(new Vector(absolute.getX(), absolute.getY())));
			MouseEvent local = m.clone();
			local.setCoordinates(localPos);
			if (absolute.contains(m.getCoordinates().toPoint())) {
				if (!isMousedOver()) {
					// if the even is in this widget, set moused over to true
					setMousedOver(true);
					// send mouse entered event
					onMouseEvent(new MouseEnteredEvent(localPos));
				}
				onMouseEvent(local);
			} else {
				if (isMousedOver()) {
					// if it is not in this widget, and we are moused over, set moused over to false
					setMousedOver(false);
					onMouseEvent(new MouseExitedEvent(localPos));

				}
				// send the external event for this
				onMouseEvent(new MouseExternalEvent(m));
			}
		} else {
			onOtherInputEvent(e);
		}

		for (Widget w : getChildren()) {
			w.onInputEvent(e);
		}
	}

	public void onMouseEvent(MouseEvent e) {
		if (e instanceof MouseClickedEvent) {
			requestFocus();
		}
	}

	public void onKeyboardEvent(KeyEvent e) {
		// For widget to implement
	}

	public void onOtherInputEvent(InputEvent e) {
		// For widget to implement
	}

	// Will re-run the layout manager if it needs to be run
	public void validate() {
		for (Widget w : m_children) {
			w.validate();
		}
	}

	public void render(Renderer renderer) {
		renderer.pushTransform();

		// System.out.println("Translating by: " + getX() + ", " + getY());
		renderer.translate(getX(), getY());
		renderWidget(renderer);

		renderer.popTransform();
	}

	public abstract void renderWidget(Renderer renderer);
}
