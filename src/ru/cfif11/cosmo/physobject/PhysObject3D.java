package ru.cfif11.cosmo.physobject;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;


public abstract class PhysObject3D extends Object3D{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2530831725509380538L;

    private SimpleVector velocity;
    private SimpleVector acceleration;

	public PhysObject3D(Object3D obj, SimpleVector velocity) {
		super(obj);
		this.velocity = velocity;
		this.acceleration = new SimpleVector();
	}

    public void calcLocation(float dT){
        calcVelocity(dT);
        SimpleVector dLocation = new SimpleVector(this.velocity);
        dLocation.scalarMul(dT);
        translate(dLocation);
    }
	
	protected void setVelocity(SimpleVector velocity) {
		this.velocity = velocity;
	}
	
	protected void setVelocity(float vx, float vy, float vz) {
		this.acceleration.set(vx, vy, vz);
	}
	
	protected void setAccelaration(float ax, float ay, float az) {
		this.acceleration.set(ax, ay, az);
	}
	
	protected void setAccelaration(SimpleVector accelearation) {
		this.acceleration = accelearation;
	}
	
	protected void calcVelocity(float dT){
		SimpleVector dVelocity = new SimpleVector(this.acceleration);
		dVelocity.scalarMul(dT);
		this.velocity.add(dVelocity);
	}
	
	protected abstract void calcAcceleration(SimpleVector force);

}
