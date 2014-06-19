package engine.core.implementation.rendering.data;

import utils.image.Texture;
import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;

/**
 * Stores the Texture of this Entity.
 */
public class TextureData extends DataComponent {
	public Texture texture;

	public TextureData() {
		super();
	}

	@Override
	public Component copy() {
		TextureData textureComponent = new TextureData();
		textureComponent.texture = new Texture(this.texture);
		return textureComponent;
	}
}
