package examples.tankarena.entities.effects;

import utils.image.Texture;
import utils.physics.Vector;
import engine.core.factory.ComponentFactory;
import engine.core.framework.Entity;
import engine.core.implementation.behavior.logic.TreeLogic;
import engine.core.implementation.network.logic.ServerLogic;
import engine.core.implementation.physics.wrappers.ShellTransformWrapper;
import engine.core.implementation.rendering.base.Animator;
import engine.core.implementation.rendering.data.AnimationData;

public class AnimatedFX extends Entity {
	public AnimatedFX(Vector position, int layer, Texture startingTexture, Animator animator) {
		super();

		ComponentFactory.addShellData(this, position, 0);

		ComponentFactory.addTextureData(this, startingTexture);

		AnimationData animation = new AnimationData();
		animation.addAnimator("effect", animator);
		this.addComponent(animation);

		animator.animate(startingTexture);

		ComponentFactory.addNetworkData(this);
		ComponentFactory.addNameData(this, "animatedfx");
		ComponentFactory.addLayerData(this, layer);
		this.addComponent(new ShellTransformWrapper());

		this.addComponent(ComponentFactory.createBasicEncoder(this));
		this.addComponent(new ServerLogic());

		TreeLogic tree = new TreeLogic();
		tree.setRoot(new AnimationAction());
		this.addComponent(tree);
	}
}
