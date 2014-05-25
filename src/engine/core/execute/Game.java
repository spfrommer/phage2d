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

	public Game(int width, int height) {
		ImageUtils.initMapping();

		PhageSplash splash = new PhageSplash();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		m_display = setupDisplay(width, height);

		m_system = new EntitySystem();
		m_rendering = new BasicRenderingActivity(m_system);

		onInit();
		initProcesses();

		splash.setVisible(false);
	}

	public void start() {
		onStart();

		startGameLoop(m_display);

		onStop();
	}

	public void setRenderingActivity(RenderingActivity rendering) {
		m_rendering = rendering;
	}

	private LWJGLDisplay setupDisplay(int width, int height) {
		LWJGLDisplay display = new LWJGLDisplay(width, height);
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

	private final int DESIRED_FPS = 50;
	private double fpsSum = 0;
	private int fpsSumCounter = 0;

	private void startGameLoop(Display display) {
		int milliSkip = 1000 / DESIRED_FPS;

		long nextGameTick = System.currentTimeMillis();
		AbsoluteTimer timer = new AbsoluteTimer();
		while (!display.destroyRequested()) {
			timer.start();
			updateProcesses(1);

			render(display);

			nextGameTick += milliSkip;
			long sleepTime = nextGameTick - System.currentTimeMillis();
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			timer.stop();
			fpsSum += 1000000000 / (timer.getTimeNanos());
			fpsSumCounter++;
			if (fpsSumCounter == 1) {
				double fps = fpsSum / fpsSumCounter;
				// System.out.println("FPS: " + fps);
				fpsSum = 0;
				fpsSumCounter = 0;
			}
		}
	}

	private void render(Display display) {
		Renderer r = display.getRenderer();

		m_viewport.lookThrough(r);

		m_rendering.render(r);

		r.setTransform(new AffineTransform());

		renderGui(r);

		display.getRenderer().setColor(Color.WHITE);
		display.render();
		display.update(DESIRED_FPS);
	}

	protected ViewPort getViewport() {
		return m_viewport;
	}

	protected EntitySystem getEntitySystem() {
		return m_system;
	}

	public abstract void renderGui(Renderer renderer);

	public abstract void onStart();

	public abstract void onStop();

	public abstract void onInit();

	public abstract void initProcesses();

	public abstract void updateProcesses(int ticks);
}
