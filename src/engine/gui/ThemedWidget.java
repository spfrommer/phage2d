package engine.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import engine.graphics.Renderer;
import engine.gui.theme.Image;
import engine.gui.theme.ImageItem;
import engine.gui.theme.Parameter;
import engine.gui.theme.Theme;
import engine.gui.theme.ThemeItem;

public abstract class ThemedWidget extends Widget {
	private static Logger logger = LoggerFactory.getLogger(ThemedWidget.class);

	@Override
	public void renderWidget(Renderer renderer) {
		Theme theme = getWidgetTheme();
		Parameter back = (Parameter) theme.get("background");
		ImageItem image = (ImageItem) back.getValue();
		if (image != null) {
			//Draw at x: 0 y: 0 because render() has already translated
			image.render(renderer, getAnimationState(), 0, 0, getSize().getWidth(), getSize().getHeight());
		}
	}
	
	
	public Theme getWidgetTheme() {
		Theme theme = getTheme();
		if (theme == null) return null;
		Theme result = (Theme) theme.get(getThemeName());
		if (result != null) theme = result;
		return theme;
	}
	
	@Override
	public Dimension getMinimumSize() {
		Theme theme = getWidgetTheme();
		
		int minHeight = 0;
		int minWidth = 0;
		
		if (theme != null) {
			ThemeItem item = theme.get("background");
			/*if (item != null && item instanceof Parameter) {
				Image image = (Image) ((Parameter) item).getValue();
				logger.info("Width: " + minWidth + " Height: " + minHeight);
				return new Dimension(image.getMinWidth(), image.getMinHeight());
			}*/
			ThemeItem mh = theme.get("minHeight");
			ThemeItem mw = theme.get("minWidth");
			if (mh != null && mh instanceof Parameter) {
				minHeight = (int) ((Parameter) mh).getValue();
			}
			if (mw != null && mw instanceof Parameter) {
				minWidth = (int) ((Parameter) mw).getValue();
			}
		}
		return new Dimension(minWidth, minHeight);
	}
	
	public abstract String getThemeName();
}
