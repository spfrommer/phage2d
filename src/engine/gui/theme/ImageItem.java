package engine.gui.theme;

import java.awt.Color;
import java.io.File;

import org.w3c.dom.Element;

import engine.graphics.Renderer;
import engine.gui.AnimationState;

public abstract class ImageItem extends ThemeItem implements Image {
	
	public ImageItem() {}
	public ImageItem(ImageItem i) {
		super(i);
	}
	
	@Override
	public void load(Theme root, ThemeItem parent, Element xmlElement, File file) {
		load(root, (ImagesFile) parent, xmlElement, file);
	}
	
	public abstract void render(Renderer r, AnimationState as, int x, int y);
	public abstract void render(Renderer r, AnimationState as, int x, int y, int width, int height);

	public abstract int getHeight();
	public abstract int getWidth();
	
	public abstract int getMinHeight();
	public abstract int getMinWidth();
	
	public abstract void tint(Color color);
	
	public abstract void load(Theme root, ImagesFile images, Element element, File file);
	
}
