package ru.cfif11.cosmo.physobject;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

/**
 * Abstract class PhysObject3D extends Object3D(jpct.lib).
 * It has physical velocity and acceleration parameters.
 */
public abstract class PhysObject3D extends Object3D{

	private static final long serialVersionUID = 2530831725509380538L;

    private SimpleVector velocity;
    private SimpleVector acceleration;

    /**
     * Create PhysObject3D based on Object3D and vector velocity, acceleration by default the zero vector
     * @param obj the Object3D
     * @param velocity the initial velocity
     */
	public PhysObject3D(Object3D obj, String name, SimpleVector velocity) {
		super(obj);
        setName(name);
		this.velocity = velocity;
		this.acceleration = new SimpleVector();
	}

    /**
     *  Calculates the new location of the PhysObject3D to a new step
     * @param dT the time step
     */
    public void calcLocation(float dT){
        calcVelocity(dT);
        SimpleVector dLocation = new SimpleVector(this.velocity);
        dLocation.scalarMul(dT);
        translate(dLocation);
    }

    /**
     * Sets the velocity of the PhysObject3D
     * @param velocity the velocity vector
     */
	protected void setVelocity(SimpleVector velocity) {
		this.velocity = velocity;
	}

    /**
     * Sets the velocity of the PhysObject3D based on the three parameters
     * @param vx the X component of the velocity
     * @param vy the Y component of the velocity
     * @param vz the Z component of the velocity
     */
	protected void setVelocity(float vx, float vy, float vz) {
		this.acceleration.set(vx, vy, vz);
	}

    /**
     * Sets the acceleration of the PhysObject3D based on the three parameters
     * @param ax the X component of the acceleration
     * @param ay the Y component of the acceleration
     * @param az the Z component of the acceleration
     */
	protected void setAcceleration(float ax, float ay, float az) {
		this.acceleration.set(ax, ay, az);
	}

    /**
     * Sets the acceleration of the PhysObject3D
     * @param acceleration the velocity vector
     */
	protected void setAccelaration(SimpleVector acceleration) {
		this.acceleration = acceleration;
	}

    /**
     *  Calculates the new velocity of the PhysObject3D to a new step
     * @param dT the time step
     */
	protected void calcVelocity(float dT){
		SimpleVector dVelocity = new SimpleVector(this.acceleration);
		dVelocity.scalarMul(dT);
		this.velocity.add(dVelocity);
	}

    /**
     * Calculates the acceleration of the PhysObject3D  by force acting on it
     * @param force the vector acting force
     */
	protected abstract void calcAcceleration(SimpleVector force);

}
