package examples.platformer;

import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Rectangle;

import utils.image.ImageUtils;
import utils.image.Texture;
import utils.physics.Vector;
import engine.core.execute.Game;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.implementation.behavior.activity.BehaviorActivity;
import engine.core.implementation.behavior.base.composite.PrioritySelectorComposite;
import engine.core.implementation.behavior.base.composite.SequencerComposite;
import engine.core.implementation.behavior.base.decorator.SuccessDecorator;
import engine.core.implementation.behavior.base.decorator.TimeDecorator;
import engine.core.implementation.behavior.base.leaf.action.executor.ActionExecutorLeaf;
import engine.core.implementation.behavior.logic.TreeLogic;
import engine.core.implementation.camera.activities.CameraActivity;
import engine.core.implementation.camera.activities.KeyboardCameraActivity;
import engine.core.implementation.camera.activities.MovementProfile;
import engine.core.implementation.physics.activities.PhysicsActivity;
import engine.core.implementation.physics.data.PhysicsData;
import engine.core.implementation.physics.wrappers.PhysicsTransformWrapper;
import engine.core.implementation.physics.wrappers.ShellTransformWrapper;
import engine.core.implementation.rendering.activities.AnimationActivity;
import engine.core.implementation.rendering.activities.ParallaxRenderingActivity;
import engine.core.implementation.rendering.logic.TextureRenderingLogic;
import engine.graphics.Renderer;
import engine.graphics.lwjgl.LWJGLKeyboard;

/**
 * A game that uses behavior trees to control a ball.
 */
public class Platformer extends Game {
	public Platformer() {
		super(1000, 500, "images-all.txt");
	}

	private PhysicsActivity m_physics;
	private AnimationActivity m_animation;
	private CameraActivity m_camera;
	private BehaviorActivity m_behavior;

	private Entity m_player;

	@Override
	public void onStart() {
		this.getEntitySystem().addEntity(makeBackground(15000, 6000));

		this.getEntitySystem().addEntity(makeScenery("palmtree3.png", new Vector(-3000, 0), 500, 3000));
		this.getEntitySystem().addEntity(makeScenery("palmtree1.png", new Vector(-1000, 0), 2000, 3000));
		this.getEntitySystem().addEntity(makeScenery("palmtree2.png", new Vector(-1000, 0), 500, 3000));
		this.getEntitySystem().addEntity(makeScenery("palmtree3.png", new Vector(0, 0), 500, 3000));
		this.getEntitySystem().addEntity(makeScenery("palmtree1.png", new Vector(500, 0), 1500, 3000));
		this.getEntitySystem().addEntity(makeScenery("palmtree3.png", new Vector(2000, 0), 500, 3000));
		this.getEntitySystem().addEntity(makeScenery("palmtree2.png", new Vector(3000, 0), 500, 3000));

		this.getEntitySystem().addEntity(makePlatform(new Vector(0, -200), 2000, 50));
		this.getEntitySystem().addEntity(makePlatform(new Vector(500, -100), 200, 150));
		// this.getEntitySystem().addEntity(makePlatform(new Vector(00, -200), 500, 50));

		m_player = makePlayer(new Vector(-500, 0));
		this.getEntitySystem().addEntity(m_player);

		this.getViewPort().getCamera().setZoom(0.2);
		ParallaxRenderingActivity rendering = new ParallaxRenderingActivity(this.getEntitySystem(), this.getViewPort()
				.getCamera());
		rendering.loadEntities();
		this.setRenderingActivity(rendering);
	}

	@Override
	public void onStop() {

	}

	@Override
	public void init() {
		m_physics = new PhysicsActivity(this.getEntitySystem());
		m_animation = new AnimationActivity(this.getEntitySystem());
		m_camera = new KeyboardCameraActivity(this.getEntitySystem(), LWJGLKeyboard.instance(), new MovementProfile(10,
				0.01));
		m_behavior = new BehaviorActivity(this.getEntitySystem());
	}

	@Override
	public void update(int ticks) {
		m_physics.update(ticks);
		m_animation.update(ticks);
		m_camera.control(this.getViewPort().getCamera(), ticks);
		m_behavior.update(ticks);
	}

	private Entity makeBackground(double width, double height) {
		Entity background = new Entity();

		ComponentFactory.addShellData(background, new Vector(0, 0), 0);

		ComponentFactory.addTextureData(background, new Texture(ImageUtils.getID("terrain2.png"), width, height));
		ComponentFactory.addNameData(background, "background");
		ComponentFactory.addLayerData(background, 0);

		background.addComponent(new ShellTransformWrapper(background));
		background.addComponent(new TextureRenderingLogic(background));
		return background;
	}

	private Entity makeScenery(String imageName, Vector position, double width, double height) {
		Entity background = new Entity();

		ComponentFactory.addShellData(background, position, 0);

		ComponentFactory.addTextureData(background, new Texture(ImageUtils.getID(imageName), width, height));
		ComponentFactory.addNameData(background, "background");
		ComponentFactory.addLayerData(background, 1);

		background.addComponent(new ShellTransformWrapper(background));
		background.addComponent(new TextureRenderingLogic(background));
		return background;
	}

	private Entity makePlayer(Vector position) {
		Entity player = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(player, position, 0, new Circle(25));
		physics.setGravity(100);
		physics.setCollisionFriction(10);
		physics.setRestitution(0.5);

		ComponentFactory.addTextureData(player, new Texture(ImageUtils.getID("blueblob.png"), 50, 50));
		ComponentFactory.addNameData(player, "player");
		ComponentFactory.addLayerData(player, 2);

		player.addComponent(new PhysicsTransformWrapper(player));
		player.addComponent(new TextureRenderingLogic(player));
		player.addComponent(new JumpControllerLogic(player));
		player.addComponent(new RollControllerLogic(player, -10000000));

		TreeLogic tree = new TreeLogic(player);

		PrioritySelectorComposite priority = new PrioritySelectorComposite(new int[] { 0, 1 });

		SequencerComposite sequencer = new SequencerComposite();
		sequencer.addChild(new LateralCollisionCondition());
		TimeDecorator timer = new TimeDecorator(100);
		timer.setChild(new ActionExecutorLeaf<RollControllerLogic>(RollControllerLogic.class));
		sequencer.addChild(timer);
		sequencer.addChild(new ActionExecutorLeaf<JumpControllerLogic>(JumpControllerLogic.class));
		priority.addChild(sequencer);

		SuccessDecorator success = new SuccessDecorator();
		success.setChild(new ActionExecutorLeaf<RollControllerLogic>(RollControllerLogic.class));
		priority.addChild(success);

		tree.setRoot(priority);

		player.addComponent(tree);
		return player;
	}

	private Entity makePlatform(Vector position, double width, double height) {
		Entity platform = new Entity();

		PhysicsData physics = ComponentFactory.addPhysicsData(platform, position, 0, new Rectangle(width, height));
		physics.setMassType(Mass.Type.INFINITE);

		ComponentFactory.addTextureData(platform, new Texture(ImageUtils.getID("darkpanel.jpg"), width, height));
		ComponentFactory.addNameData(platform, "platform");
		ComponentFactory.addLayerData(platform, 2);

		platform.addComponent(new PhysicsTransformWrapper(platform));
		platform.addComponent(new TextureRenderingLogic(platform));
		return platform;
	}

	public static void main(String[] args) {
		Platformer platformer = new Platformer();
		platformer.start();
	}

	@Override
	public void renderGui(Renderer renderer) {

	}
}
