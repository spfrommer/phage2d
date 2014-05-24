package engine.core.implementation.rendering.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import engine.core.framework.Aspect;
import engine.core.framework.AspectActivity;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.camera.base.Camera;
import engine.core.implementation.rendering.data.LayerData;
import engine.core.implementation.rendering.logic.RenderingLogic;
import engine.graphics.Renderer;

public class ParallaxRenderingActivity extends AspectActivity implements
		RenderingActivity {
	private ComponentType m_renderingType;
	private ComponentType m_layerType;

	/**
	 * A layer-ordered list that contains the Entities that have to be rendered.
	 */
	private List<Entity> m_entities;

	private Camera m_camera;

	{
		m_entities = new ArrayList<Entity>();
		m_renderingType = TypeManager.getType(RenderingLogic.class);
		m_layerType = TypeManager.getType(LayerData.class);
	}

	public ParallaxRenderingActivity(EntitySystem system, Camera camera) {
		super(system, new Aspect(TypeManager.getType(RenderingLogic.class),
				TypeManager.getType(LayerData.class)));

		m_camera = camera;
	}

	@Override
	public void entityAdded(Entity entity) {
		LayerData layer = (LayerData) entity.getComponent(m_layerType);

		if (layer.layer < 0)
			throw new RuntimeException("Cannot render a layer of less than 0!");

		m_entities.add(entity);
		sortList();
	}

	@Override
	public void entityRemoved(Entity entity) {
		m_entities.remove(entity);
		sortList();
	}

	@Override
	public void render(Renderer renderer) {
		for (Entity entity : m_entities) {
			RenderingLogic rendering = (RenderingLogic) entity
					.getComponent(m_renderingType);
			LayerData layer = (LayerData) entity.getComponent(m_layerType);

			float scale = (layer.layer + 1);
			int transX = (int) (-scale * m_camera.getX() * 0.1);
			int transY = (int) (-scale * m_camera.getY() * 0.1);
			renderer.scale(scale, scale);
			renderer.translate(transX, transY);

			rendering.render(renderer);

			renderer.translate(-transX, -transY);
			renderer.scale(1 / scale, 1 / scale);
		}
	}

	private void sortList() {
		Collections.sort(m_entities, new LayerComparator());
	}

	private class LayerComparator implements Comparator<Entity> {
		@Override
		public int compare(Entity entity1, Entity entity2) {
			LayerData layer1 = (LayerData) entity1.getComponent(m_layerType);
			LayerData layer2 = (LayerData) entity2.getComponent(m_layerType);
			return layer1.layer - layer2.layer;
		}
	}
}
