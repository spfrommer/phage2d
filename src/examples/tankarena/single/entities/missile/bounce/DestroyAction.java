package examples.tankarena.single.entities.missile.bounce;

import java.util.ArrayList;

import utils.image.ImageUtils;
import utils.image.Texture;
import utils.physics.Vector;
import engine.core.framework.Entity;
import engine.core.framework.EntitySystem;
import engine.core.framework.component.type.ComponentType;
import engine.core.framework.component.type.TypeManager;
import engine.core.implementation.behavior.base.ExecutionState;
import engine.core.implementation.behavior.base.Node;
import engine.core.implementation.behavior.base.leaf.action.ActionLeaf;
import engine.core.implementation.extras.data.DamageData;
import engine.core.implementation.extras.logic.DamageHandlerLogic;
import engine.core.implementation.physics.wrappers.TransformWrapper;
import engine.core.implementation.rendering.base.Animator;
import examples.tankarena.single.entities.effects.AnimatedFX;

/**
 * Handles how the Bouncy missile should die.
 * 
 * @eng.dependencies DamageData, BounceData, TransformWrapper
 */
public class DestroyAction extends ActionLeaf {
	private DamageData m_damage;
	private BounceData m_bounce;
	private TransformWrapper m_transform;

	private ComponentType m_damageType;

	private Entity m_entity;

	{
		m_damageType = TypeManager.typeOf(DamageHandlerLogic.class);
	}

	public DestroyAction() {
		super();
	}

	@Override
	public boolean load(Entity entity) {
		m_entity = entity;
		try {
			m_damage = (DamageData) entity.getComponent(TypeManager.typeOf(DamageData.class));
			m_bounce = (BounceData) entity.getComponent(TypeManager.typeOf(BounceData.class));
			m_transform = (TransformWrapper) entity.getComponent(TypeManager.typeOf(TransformWrapper.class));
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	@Override
	public Node copy() {
		return new DestroyAction();
	}

	@Override
	public ExecutionState update(int ticks) {
		if (!(m_bounce.bouncedAgainst != null && m_bounce.bouncedAgainst.hasComponent(m_damageType))
				&& !(m_bounce.bounceCount >= m_bounce.maxBounces)) {
			return ExecutionState.FAILURE;
		}

		System.out.println(this.getContext().getEntityContext());
		EntitySystem system = this.getContext().getEntityContext().getSystem();
		Entity collided = m_bounce.bouncedAgainst;
		system.removeEntity(m_entity);
		system.addEntity(makeExplosion(m_transform.getPosition(), 30));
		if (!collided.hasComponent(m_damageType)) {
			System.out.println("Did no damage");
			return ExecutionState.FAILURE;
		} else {
			DamageHandlerLogic damageHandler = (DamageHandlerLogic) collided.getComponent(m_damageType);
			damageHandler.handleDamage(m_damage.damage);
			System.out.println("Did damage: " + m_damage.damage);
			return ExecutionState.SUCCESS;
		}
	}

	private Entity makeExplosion(Vector position, double size) {
		Texture explosion = new Texture(ImageUtils.getID("explosion.png"), size, size);
		ArrayList<Texture> textures = new ArrayList<Texture>();
		textures.add(explosion);
		Animator animator = new Animator(textures, 1);
		return new AnimatedFX(position, 1, explosion, animator);
	}
}
