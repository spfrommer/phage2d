package engine.core.implementation.rendering.base;

import java.util.ArrayList;
import java.util.List;

import utils.image.Texture;

/**
 * Animates a List of Textures.
 */
public class Animator {
	private List<Texture> m_animationSequence;
	private Texture m_toAnimate;
	private int m_ticksPerFrame;
	// Ticks since last update
	private long m_ticksElapsed = 0;
	private int m_currentFrame = 0;

	public Animator(ArrayList<Texture> animationSequence, int ticksPerFrame) {
		m_animationSequence = animationSequence;
		m_ticksPerFrame = ticksPerFrame;
	}

	public void animate(Texture texture) {
		m_toAnimate = texture;
		m_ticksElapsed = m_ticksPerFrame;
		m_currentFrame = 0;
	}

	public void updateAnimation(int ticks) {
		if (m_toAnimate != null && m_currentFrame < m_animationSequence.size()) {
			m_ticksElapsed += ticks;
			while (m_ticksElapsed >= m_ticksPerFrame && m_currentFrame < m_animationSequence.size()) {
				Texture frame = m_animationSequence.get(m_currentFrame);
				m_toAnimate.setImageID(frame.getImageID());
				m_toAnimate.setWidth(frame.getWidth());
				m_toAnimate.setHeight(frame.getHeight());
				m_currentFrame++;
				m_ticksElapsed -= m_ticksPerFrame;
			}
			/*m_timeSinceLastUpdate += timeNanos;
			while (m_timeSinceLastUpdate >= m_frameIntervalNanos && m_currentFrame < m_animationSequence.size()) {
				Texture frame = m_animationSequence.get(m_currentFrame);
				m_toAnimate.setImageID(frame.getImageID());
				m_toAnimate.setWidth(frame.getWidth());
				m_toAnimate.setHeight(frame.getHeight());
				m_currentFrame++;
				m_timeSinceLastUpdate -= m_frameIntervalNanos;
			}*/
		}
	}
}
