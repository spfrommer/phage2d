package engine.core.implementation.rendering.base;

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
	private long m_ticksElapsed;
	private int m_currentFrame;

	public Animator(List<Texture> animationSequence, int ticksPerFrame) {
		m_animationSequence = animationSequence;
		m_ticksPerFrame = ticksPerFrame;
	}

	/**
	 * Rewinds the animation
	 * 
	 * @param texture
	 */
	public void animate(Texture texture) {
		m_toAnimate = texture;
		m_ticksElapsed = m_ticksPerFrame;
		m_currentFrame = 0;
	}

	/**
	 * Updates the animation - should be called by AnimationActivity
	 * 
	 * @param ticks
	 */
	public void updateAnimation(int ticks) {
		if (m_toAnimate != null && m_currentFrame < m_animationSequence.size()) {
			m_ticksElapsed += ticks;
			System.out.println("animating");
			while (m_ticksElapsed >= m_ticksPerFrame && m_currentFrame < m_animationSequence.size()) {
				Texture frame = m_animationSequence.get(m_currentFrame);
				m_toAnimate.setImageID(frame.getImageID());
				m_toAnimate.setWidth(frame.getWidth());
				m_toAnimate.setHeight(frame.getHeight());
				m_currentFrame++;
				m_ticksElapsed -= m_ticksPerFrame;
			}
			/*
			 * m_timeSinceLastUpdate += timeNanos; while (m_timeSinceLastUpdate
			 * >= m_frameIntervalNanos && m_currentFrame <
			 * m_animationSequence.size()) { Texture frame =
			 * m_animationSequence.get(m_currentFrame);
			 * m_toAnimate.setImageID(frame.getImageID());
			 * m_toAnimate.setWidth(frame.getWidth());
			 * m_toAnimate.setHeight(frame.getHeight()); m_currentFrame++;
			 * m_timeSinceLastUpdate -= m_frameIntervalNanos; }
			 */
		}
	}

	/**
	 * Returns if this animation has finished playing
	 * 
	 * @return
	 */
	public boolean isFinished() {
		return (m_currentFrame >= m_animationSequence.size());
	}

	/**
	 * Returns if the animation is rewinded to the first frame and is ready to play.
	 * 
	 * @return
	 */
	public boolean isReset() {
		return (m_currentFrame == 0);
	}
}
