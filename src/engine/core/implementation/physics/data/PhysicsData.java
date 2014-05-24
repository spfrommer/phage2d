package engine.core.implementation.physics.data;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Mass;

import utils.physics.Vector;
import engine.core.framework.Entity;
import engine.core.framework.component.Component;
import engine.core.framework.component.DataComponent;
import engine.core.implementation.physics.logic.filter.PhysicsFilter;

/**
 * Stores the Body and BodyFixture for physics computations.
 */
public class PhysicsData extends DataComponent {
	private Body m_body;
	private BodyFixture m_fixture;

	{
		m_body = new Body();
	}

	public PhysicsData(Convex convex) {
		super();
		initializeData(convex);
	}

	public PhysicsData(Entity parent, Convex convex) {
		super(parent);
		initializeData(convex);

		m_fixture.setFilter(new PhysicsFilter(this.getEntity()));
	}

	private final void initializeData(Convex convex) {
		setCenter(new Vector(0, 0));
		setPosition(new Vector(0, 0));

		m_fixture = new BodyFixture(convex);
		m_body.addFixture(m_fixture);

		setMass(m_fixture.createMass().getMass());
		setInertia(m_fixture.createMass().getInertia());
		setGravity(0);
		setID(-1);
	}

	/**
	 * Sets whether this structure is asleep in the physics engine
	 * 
	 * @param asleep
	 */
	public void setAsleep(boolean asleep) {
		m_body.setAsleep(asleep);
	}

	/**
	 * Returns whether this structure is asleep in the physics engine
	 * 
	 * @return
	 */
	public boolean isAsleep() {
		return m_body.isAsleep();
	}

	/**
	 * Sets the movement friction
	 * 
	 * @param friction
	 */
	public void setMovementFriction(double friction) {
		m_body.setLinearDamping(friction);
	}

	/**
	 * Gets the movement friction
	 * 
	 * @param friction
	 * @return
	 */
	public double getMovementFriction() {
		return m_body.getLinearDamping();
	}

	/**
	 * Sets the rotational friction
	 * 
	 * @param friction
	 */
	public void setRotationalFriction(double friction) {
		m_body.setAngularDamping(friction);
	}

	/**
	 * Gets the rotational friction
	 * 
	 * @param friction
	 * @return
	 */
	public double getRotationalFriction() {
		return m_body.getAngularDamping();
	}

	/**
	 * Sets the friction when colliding with other objects
	 * 
	 * @param friction
	 */
	public void setCollisionFriction(double friction) {
		m_fixture.setFriction(friction);
	}

	/**
	 * Gets the friction when colliding with other objects
	 * 
	 * @param friction
	 * @return
	 */
	public double getCollisionFriction() {
		return m_fixture.getFriction();
	}

	/**
	 * sets the restitution, or elasticity.
	 * 
	 * @param restitution
	 */
	public void setRestitution(double restitution) {
		m_fixture.setRestitution(restitution);
	}

	/**
	 * Gets the restitution, or elasticity.
	 * 
	 * @return
	 */
	public double getRestitution() {
		return m_fixture.getRestitution();
	}

	/**
	 * Sets the linear velocity of this object
	 * 
	 * @param velocity
	 */
	public void setVelocity(Vector velocity) {
		m_body.setLinearVelocity(velocity.getX(), velocity.getY());
	}

	/**
	 * Returns the linear velocity of this object
	 * 
	 * @return
	 */
	public Vector getVelocity() {
		return (new Vector(m_body.getLinearVelocity()));
	}

	/**
	 * Sets the angular, or rotational, velocity of this structure
	 * 
	 * @param rotationalVelocity
	 */
	public void setRotationalVelocity(double rotationalVelocity) {
		m_body.setAngularVelocity(rotationalVelocity);
	}

	/**
	 * Returns the angular, or rotational, velocity of this structure
	 * 
	 * @return
	 */
	public double getRotationalVelocity() {
		return m_body.getAngularVelocity();
	}

	/**
	 * Set the effect of gravity on the structure - 0 is no gravity, 1 is normal gravity
	 * 
	 * @param gravity
	 */
	public void setGravity(double gravity) {
		m_body.setGravityScale(gravity);
	}

	/**
	 * Get the effect gravity has on this structure - 0 is no gravity, 1 is normal gravity
	 * 
	 * @param mt
	 * @return
	 */
	public double getGravity() {
		return m_body.getGravityScale();
	}

	/**
	 * Sets the mass of this structure
	 * 
	 * @param mass
	 */
	public void setMass(double mass) {
		m_body.setMass(new Mass(m_body.getMass().getCenter(), mass, m_body.getMass().getInertia()));
	}

	public double getMass() {
		return m_body.getMass().getMass();
	}

	/**
	 * Sets the inertia of this structure
	 * 
	 * @param inertia
	 */
	public void setInertia(double inertia) {
		m_body.setMass(new Mass(m_body.getMass().getCenter(), m_body.getMass().getMass(), inertia));
	}

	public double getInertia() {
		return m_body.getMass().getInertia();
	}

	/**
	 * Sets the mass type
	 * 
	 * @param mt
	 */
	public void setMassType(Mass.Type mt) {
		m_body.setMassType(mt);
	}

	public Mass.Type getMassType() {
		return m_body.getMass().getType();
	}

	/**
	 * A Bullet body is one that moves very quickly - therefore, continuous collision detection must be performed on all
	 * other bodies every step to avoid missing a collision - this is very expensive and should not be used unless
	 * necessary
	 * 
	 * @param isBullet
	 */
	public void setBullet(boolean isBullet) {
		m_body.setBullet(isBullet);
	}

	/**
	 * returns if this structure is of type bullet - see setBullet(boolean isBullet)
	 * 
	 * @return
	 */
	public boolean isBullet() {
		return m_body.isBullet();
	}

	/**
	 * Set the rotation in degrees (from -180 at south to 0 at north to 180 south)
	 * 
	 * @param degrees
	 */
	public void setRotation(double degrees) {
		m_body.getTransform().setRotation(Math.toRadians(degrees));
		// rotationChanged();
	}

	/**
	 * Get the rotation in degrees (from -180 at south to 0 at north to 180 south)
	 * 
	 * @return
	 */
	public double getRotation() {
		return Math.toDegrees(m_body.getTransform().getRotation());
	}

	/**
	 * Sets the center of this object in world coordinates
	 * 
	 * @param position
	 */
	public void setPosition(Vector position) {
		m_body.getTransform().setTranslation(position.getX(), position.getY());
		// positionChanged();
	}

	/**
	 * Returns the center of this object in world coordinates
	 * 
	 * @return
	 */
	public Vector getPosition() {
		return (new Vector(m_body.getTransform().getTranslationX(), m_body.getTransform().getTranslationY()));
	}

	/**
	 * Sets the offset of the center of the structure from the middle of the image
	 * 
	 * @param center
	 */
	public void setCenter(Vector center) {
		m_body.setMass(new Mass(center.toPhysicsVector(), m_body.getMass().getMass(), m_body.getMass().getInertia()));
	}

	/**
	 * Returns the offset of the center of the structure from the middle of the image
	 * 
	 * @return
	 */
	public Vector getCenter() {
		return new Vector(m_body.getMass().getCenter());
	}

	/**
	 * Sets the collision shape
	 * 
	 * @param convex
	 */
	public void setConvex(Convex convex) {
		double friction = getCollisionFriction();
		m_fixture = new BodyFixture(convex);
		m_fixture.setFriction(friction);
	}

	/**
	 * Gets the collision shape
	 * 
	 * @return
	 */
	public Convex getConvex() {
		return m_fixture.getShape();
	}

	/**
	 * Loads the id into the body
	 * 
	 * @param id
	 */
	public void setID(int id) {
		m_body.setUserData(id);
	}

	/**
	 * Returns the physics id in the body
	 * 
	 * @return
	 */
	public int getID() {
		return (Integer) (m_body.getUserData());
	}

	/**
	 * Translates the center of this structure in world coordinates
	 * 
	 * @param translate
	 */
	public void translateCenter(Vector translate) {
		setPosition(getPosition().add(translate));
		// positionChanged();
	}

	/**
	 * Will apply a force to the structure - this will be processed upon the update by the physics engine
	 * 
	 * @param impulse
	 */
	public void applyForce(Vector impulse) {
		m_body.applyForce(impulse.toPhysicsVector());
	}

	/**
	 * Rotates the structure around the center by the specified number of degrees
	 * 
	 * @param degrees
	 */
	public void rotateAroundCenter(double degrees) {
		m_body.rotateAboutCenter(Math.toRadians(degrees));
		// rotationChanged();
	}

	/**
	 * Applies a torque around the center
	 * 
	 * @param torque
	 */
	public void applyTorque(double torque) {
		m_body.applyTorque(torque);
	}

	/**
	 * Adds the body to the dyn4j world
	 * 
	 * @param world
	 */
	public void addToWorld(World world) {
		world.addBody(m_body);
	}

	/**
	 * Removes it from that world
	 * 
	 * @param world
	 */
	public void removeFromWorld(World world) {
		world.removeBody(m_body);
	}

	/**
	 * Will return true if the linear velocity is greater than 1 or the angular velocity is greater than 0.1.
	 * 
	 * @return
	 */
	public boolean isMoving() {
		return (m_body.getLinearVelocity().getMagnitude() > 1 || Math.abs(m_body.getAngularVelocity()) > 0.1);
	}

	/**
	 * Tests whether the body contains these coordinates in its translated convex
	 * 
	 * @param worldCoordinates
	 * @return
	 */
	public boolean contains(Vector worldCoordinates) {
		return m_body.contains(worldCoordinates.toPhysicsVector());
	}

	/**
	 * Returns the collision shape
	 * 
	 * @return
	 */

	public Shape getCollisionShape() {
		AABB bounds = m_fixture.getShape().createAABB();
		Rectangle2D rectangle = new Rectangle2D.Double(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(),
				bounds.getHeight());
		AffineTransform at = new AffineTransform();
		at.translate(getPosition().getX() + getCenter().getX(), getPosition().getY() + getCenter().getY());
		at.rotate(m_body.getTransform().getRotation());
		return at.createTransformedShape(rectangle);
	}

	@Override
	public Component copy() {
		PhysicsData component = new PhysicsData(this.getConvex());

		component.setAsleep(this.isAsleep());
		component.setBullet(this.isBullet());
		component.setCenter(this.getCenter());
		component.setGravity(this.getGravity());
		component.setMass(this.getMass());
		component.setInertia(this.getInertia());
		component.setMassType(this.getMassType());
		component.setPosition(this.getPosition());
		component.setRotation(this.getRotation());
		component.setVelocity(this.getVelocity());
		component.setRotationalVelocity(this.getRotationalVelocity());

		component.setCollisionFriction(this.getCollisionFriction());
		component.setMovementFriction(this.getMovementFriction());
		component.setRotationalFriction(this.getRotationalFriction());

		// component.setCollisionFilter(this.getCollisionFilter());
		component.setID(this.getID());

		return component;
	}

	@Override
	public void setEntity(Entity parent) {
		super.setEntity(parent);
		m_fixture.setFilter(new PhysicsFilter(this.getEntity()));
	}
}
