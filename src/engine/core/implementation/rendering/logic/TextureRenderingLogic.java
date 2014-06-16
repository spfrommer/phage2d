package engine.core.implementation.rendering.logic;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.wrappers.TransformWrapper;
import engine.core.implementation.rendering.data.TextureData;
import engine.graphics.Renderer;

/**
 * Renders a Texture at a certain position and rotates this around its center.
 * 
 * @eng.dependencies TextureData, TransformWrapper
 */
public class TextureRenderingLogic extends RenderingLogic {
	private TextureData m_textureData;
	private TransformWrapper m_transform;

	public TextureRenderingLogic() {
		super(new Aspect(TypeManager.getType(TextureData.class), TypeManager.getType(TransformWrapper.class)));
	}

	public TextureRenderingLogic(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(TextureData.class), TypeManager.getType(TransformWrapper.class)));
	}

	@Override
	public void render(Renderer renderer) {
		renderer.pushTransform();

		renderer.translate((float) m_transform.getPosition().getX(), (float) m_transform.getPosition().getY());
		renderer.rotate((float) Math.toRadians(m_transform.getRotation()));
		renderer.translate((float) m_transform.getCenter().getX(), (float) m_transform.getCenter().getY());

		renderer.scale(1, -1);
		renderer.translate((float) -m_textureData.texture.getWidth() / 2,
				(float) -m_textureData.texture.getHeight() / 2);
		renderer.drawImage(m_textureData.texture.getImage(), 0, 0, (float) m_textureData.texture.getWidth(),
				(float) m_textureData.texture.getHeight());

		renderer.popTransform();
	}

	@Override
	public void loadDependencies() {
		m_textureData = (TextureData) this.loadDependency(TypeManager.getType(TextureData.class));
		m_transform = (TransformWrapper) this.loadDependency(TypeManager.getType(TransformWrapper.class));
	}

	@Override
	public Component copy() {
		return new TextureRenderingLogic();
	}
}
