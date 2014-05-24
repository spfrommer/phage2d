package engine.core.implementation.rendering.data;

import utils.image.Texture;
import engine.core.framework.component.DataComponent;
import engine.core.implementation.network.base.decoding.DataDecoder;

/**
 * Decodes a TextureData.
 */
public class TextureDecoder implements DataDecoder {
	@Override
	public void decode(DataComponent component, String[] data) {
		TextureData texture = (TextureData) component;
		texture.texture = new Texture(Integer.parseInt(data[0]), Double.parseDouble(data[1]),
				Double.parseDouble(data[2]));
	}
}
