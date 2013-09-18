package ru.cfif11.cosmo.physobject;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;


public abstract class PhysObject3D extends Object3D{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2530831725509380538L;

	public PhysObject3D(Object3D obj, SimpleVector velocity) {
		super(obj);
		this.velocity = velocity;
		this.acceleration = new SimpleVector();
	}

	private SimpleVector velocity;
	private SimpleVector acceleration;
	//protected Object3D object3D;
	
	static public PhysObject3D load3DS (String filename, float scale, SimpleVector velocity) {
		PhysObject3D obj =  (PhysObject3D)Loader.load3DS(filename, scale)[0];
		obj.setVelocity(velocity);
		return obj;
	}
		
	/*public PhysObject3D(String fileName, float scale, SimpleVector velocity) {
		this.object3D = Loader.load3DS(fileName, scale)[0];
		this.velocity = velocity;
	}
	
	public PhysObject3D(String fileName, float scale) {
		this.object3D = Loader.load3DS(fileName, scale)[0];
		this.velocity = new SimpleVector();
	}*/
	
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
	
	public void calcLocation(float dT){
		calcVelocity(dT);
		SimpleVector dLocation = new SimpleVector(this.velocity);
		dLocation.scalarMul(dT);
		translate(dLocation);
	}
	
	protected abstract void calcAcceleration(SimpleVector force);
	
	
	

}
