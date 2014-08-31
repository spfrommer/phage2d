package migrate.gui.test;

import java.awt.Rectangle;

import migrate.gui.Dimension;
import migrate.gui.widgets.Label;
import engine.graphics.Renderer;

public class SimpleLabel extends Label {
	//private static Logger logger = LoggerFactory.getLogger(SimpleLabel.class);
	
	public SimpleLabel() {
		setFont(SimpleThemeConstants.LABEL_FONT);
		setColor(SimpleThemeConstants.COLOR_SOLID);
	}
	@Override
	public Dimension getMinSize() {
		Rectangle boundaries = getFont().getBounds(getText());
		return new Dimension((int) (boundaries.getX() + boundaries.getWidth()),
								(int) (boundaries.getY() + boundaries.getHeight()));
	}
	@Override
	public void renderWidget(Renderer r) {
		
		Rectangle textBounds = getFont().getBounds(getText());
		
		int middleX = (int) (0.5 * getWidth());
		int middleY = (int) (0.5 * getHeight());
		
		int x = middleX - (int) (0.5 * textBounds.getWidth());
		//Use the size and not the height so that the
		//descenders won't be included as much
		int y = middleY + (int) (0.5 * getFont().getSize());
		//logger.debug("Drawing text at: (" + x + ", " + y + ") total size: " + getWidth() + " " + getHeight() +
		//				" middleX " + middleX + " middleY " + middleY);
		r.setFont(getFont());
		r.setColor(getColor());
		r.drawString(getText(), x, y);
	}	
}
