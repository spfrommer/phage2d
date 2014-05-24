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
import engine.core.implementation.rendering.data.LayerData;
import engine.core.implementation.rendering.logic.RenderingLogic;
import engine.graphics.Renderer;

/**
 * Manages layers as well as calls RenderingLogic in Entities.
 * 
 * @eng.dependencies RenderingLogic, LayerData
 */
public class BasicRenderingActivity extends AspectActivity implements RenderingActivity {
	private ComponentType m_renderingType;
	private ComponentType m_layerType;

	/**
	 * A layer-ordered list that contains the Entities that have to be rendered.
	 */
	private List<Entity> m_entities;

	public BasicRenderingActivity(EntitySystem system) {
		super(system, new Aspect(TypeManager.getType(RenderingLogic.class), TypeManager.getType(LayerData.class)));

		m_entities = new ArrayList<Entity>();
		m_renderingType = TypeManager.getType(RenderingLogic.class);
		m_layerType = TypeManager.getType(LayerData.class);
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
			RenderingLogic rendering = (RenderingLogic) entity.getComponent(m_renderingType);
			rendering.render(renderer);
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
