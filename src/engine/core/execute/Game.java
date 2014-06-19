package engine.core.execute;

import java.awt.geom.AffineTransform;

import utils.image.ImageUtils;
import utils.timers.AbsoluteTimer;
import engine.core.framework.EntitySystem;
import engine.core.implementation.camera.base.Camera;
import engine.core.implementation.camera.base.Display;
import engine.core.implementation.camera.base.SingleViewPortLayout;
import engine.core.implementation.camera.base.ViewPort;
import engine.core.implementation.rendering.activities.BasicRenderingActivity;
import engine.core.implementation.rendering.activities.RenderingActivity;
import engine.graphics.Color;
import engine.graphics.Renderer;
import engine.graphics.lwjgl.LWJGLDisplay;

/**
 * A single player Game.
 */
public abstract class Game {
	private EntitySystem m_system;

	private RenderingActivity m_rendering;

	private ViewPort m_viewport;
	private Camera m_camera;
	private Display m_display;

	private int m_desiredFPS = 40;

	{
		m_system = new EntitySystem();
		m_rendering = new BasicRenderingActivity(m_system);
	}

	public Game(int width, int height, String images) {
		ImageUtils.initMapping(images);

		PhageSplash splash = new PhageSplash();

		m_display = setupDisplay(width, height);

		init();

		splash.setVisible(false);
	}

	public void start() {
		onStart();

		startGameLoop(m_display);

		onStop();
	}

	/**
	 * Sets the desired FPS of this game. Set by default to 60.
	 * 
	 * @param fps
	 */
	public void setFPS(int fps) {
		m_desiredFPS = fps;
	}

	/**
	 * Sets the RenderingActivity that this game should use when rendering.
	 * 
	 * @param rendering
	 */
	public void setRenderingActivity(RenderingActivity rendering) {
		m_rendering = rendering;
	}

	private LWJGLDisplay setupDisplay(int width, int height) {
		LWJGLDisplay display = new LWJGLDisplay(width, height, true);
		display.init();

		SingleViewPortLayout layout = new SingleViewPortLayout(display);

		m_camera = new Camera();
		m_camera.setX(0);
		m_camera.setY(0);

		m_viewport = new ViewPort(m_camera);
		layout.setViewPort(m_viewport);

		layout.validate();

		return display;
	}

	private double fpsSum = 0;
	private int fpsSumCounter = 0;

	private void startGameLoop(Display display) {
		AbsoluteTimer timer = new AbsoluteTimer();
		int milliSkip = 1000 / m_desiredFPS;

		long nextGameTick = System.currentTimeMillis();
		while (!display.destroyRequested()) {
			// timer.start();
			m_system.update();
			update(1);
			render(display);

			nextGameTick += milliSkip;
			long sleepTime = nextGameTick - System.currentTimeMillis();
			System.out.println("Next time: " + nextGameTick + "; current time: " + System.currentTimeMillis());
			if (sleepTime > 0) {
				System.out.println("Sleeping: " + sleepTime);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// capFrameRate();
			// timer.stop();

			/*fpsSum += 1000000000 / (timer.getTimeNanos());
			fpsSumCounter++;
			if (fpsSumCounter == 1) {
				double fps = fpsSum / fpsSumCounter;
				// System.out.println("FPS: " + fps);
				fpsSum = 0;
				fpsSumCounter = 0;
			}*/
		}
	}

	private double m_start = 0;
	private double m_diff;
	private double m_wait;

	private void capFrameRate() {
		m_wait = 1 / m_desiredFPS;
		m_diff = System.currentTimeMillis() - m_start;
		System.out.println(m_diff < m_wait);
		if (m_diff < m_wait) {
			System.out.println("Sleeping");
			try {
				System.out.println("Waiting");
				Thread.sleep((long) (m_wait - m_diff));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		m_start = System.currentTimeMillis();
	}

	private void render(Display display) {
		Renderer r = display.getRenderer();

		m_viewport.lookThrough(r);
		m_rendering.render(r);

		r.setTransform(new AffineTransform());

		renderGui(r);

		display.getRenderer().setColor(Color.WHITE);
		display.render();
		display.update();
	}

	protected ViewPort getViewPort() {
		return m_viewport;
	}

	protected EntitySystem getEntitySystem() {
		return m_system;
	}

	public abstract void onStart();

	public abstract void onStop();

	public abstract void init();

	public abstract void update(int ticks);

	public abstract void renderGui(Renderer renderer);
}
