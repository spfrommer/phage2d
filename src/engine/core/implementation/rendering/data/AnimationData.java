package engine.core.implementation.rendering.data;

import java.util.Collection;
import java.util.HashMap;

import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;
import engine.core.implementation.rendering.base.Animator;

/**
 * Stores a String key to Animator Map.
 */
public class AnimationData extends DataComponent {
	private HashMap<String, Animator> m_animators;

	{
		m_animators = new HashMap<String, Animator>();
	}

	public AnimationData() {
		super();
	}

	public AnimationData(Entity parent) {
		super(parent);
	}

	/**
	 * Adds an Animator to this component.
	 * 
	 * @param key
	 * @param animator
	 */
	public void addAnimator(String key, Animator animator) {
		if (m_animators.containsKey(key))
			throw new RuntimeException("AnimationComponent already has key: " + key);
		m_animators.put(key, animator);
	}

	public void removeAnimator(String key) {
		m_animators.remove(key);
	}

	public Animator getAnimator(String key) {
		return m_animators.get(key);
	}

	public Collection<Animator> getAnimators() {
		return m_animators.values();
	}

	@Override
	public Component copy() {
		AnimationData animation = new AnimationData();
		for (String key : m_animators.keySet()) {
			animation.addAnimator(key, m_animators.get(key));
		}
		return animation;
	}

}
