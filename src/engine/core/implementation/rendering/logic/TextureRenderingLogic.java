package engine.core.implementation.rendering.logic;

import engine.core.framework.Aspect;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.physics.wrappers.CenterWrapper;
import engine.core.implementation.physics.wrappers.PositionWrapper;
import engine.core.implementation.physics.wrappers.RotationWrapper;
import engine.core.implementation.rendering.data.TextureData;
import engine.graphics.Renderer;

/**
 * Renders a Texture at a certain position and rotates this around its center.
 * 
 * @eng.dependencies TextureData, PositionWrapper, RotationWrapper, CenterWrapper
 */
public class TextureRenderingLogic extends RenderingLogic {
	private TextureData m_textureData;
	private PositionWrapper m_position;
	private RotationWrapper m_rotation;
	private CenterWrapper m_center;

	public TextureRenderingLogic() {
		super(new Aspect(TypeManager.getType(TextureData.class), TypeManager.getType(PositionWrapper.class),
				TypeManager.getType(RotationWrapper.class), TypeManager.getType(CenterWrapper.class)));
	}

	public TextureRenderingLogic(Entity parent) {
		super(parent, new Aspect(TypeManager.getType(TextureData.class), TypeManager.getType(PositionWrapper.class),
				TypeManager.getType(RotationWrapper.class), TypeManager.getType(CenterWrapper.class)));
	}

	@Override
	public void render(Renderer renderer) {
		renderer.pushTransform();

		renderer.translate((float) m_position.getPosition().getX(), (float) m_position.getPosition().getY());
		renderer.rotate((float) Math.toRadians(m_rotation.getRotation()));
		renderer.translate((float) m_center.getCenter().getX(), (float) m_center.getCenter().getY());

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
		m_position = (PositionWrapper) this.loadDependency(TypeManager.getType(PositionWrapper.class));
		m_rotation = (RotationWrapper) this.loadDependency(TypeManager.getType(RotationWrapper.class));
		m_center = (CenterWrapper) this.loadDependency(TypeManager.getType(CenterWrapper.class));
	}

	@Override
	public Component copy() {
		return new TextureRenderingLogic();
	}
}
