package engine.core.implementation.rendering.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import engine.core.framework.component.Component;
import engine.core.implementation.network.base.encoding.DataEncoder;

/**
 * Encodes a TextureData.
 */
public class TextureEncoder implements DataEncoder {
	@Override
	public Element encode(Component component, Document doc) {
		TextureData textureComponent = (TextureData) component;

		Element textureElement = doc.createElement(TextureData.class.getName());

		Element textureIDElement = doc.createElement("id");
		textureIDElement.appendChild(doc.createTextNode("" + textureComponent.texture.getImageID()));
		textureElement.appendChild(textureIDElement);

		Element textureWidthElement = doc.createElement("width");
		textureWidthElement.appendChild(doc.createTextNode("" + textureComponent.texture.getWidth()));
		textureElement.appendChild(textureWidthElement);

		Element textureHeightElement = doc.createElement("height");
		textureHeightElement.appendChild(doc.createTextNode("" + textureComponent.texture.getHeight()));
		textureElement.appendChild(textureHeightElement);

		return textureElement;
	}
}
