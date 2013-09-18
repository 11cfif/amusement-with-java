package ru.cfif11.cosmo.physobject;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

public class MassObject3D extends PhysObject3D implements HasMassInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6141176990423946936L;
	private double mass;
	
	static public MassObject3D load3DS (String filename, float scale, SimpleVector velocity, double mass) {
		MassObject3D obj =  (MassObject3D)Loader.load3DS(filename, scale)[0];
		obj.setVelocity(velocity);
		obj.setMass(mass);
		return obj;
	}
	
	static public MassObject3D getSphere (int faces, float scale, SimpleVector velocity, double mass) {
		MassObject3D obj =  (MassObject3D)Primitives.getSphere(faces, scale);
		obj.setVelocity(velocity);
		obj.setMass(mass);
		return obj;
	}
	
	public MassObject3D(Object3D obj, SimpleVector velocity, double mass) {
		super(obj, velocity);
		this.mass = mass;
	}
	
	public double getMass() {
		return mass;
	}
	
	void setMass(double mass) {
		this.mass = mass;
	}
	
	/*public MasObject3D(String fileName, float scale, SimpleVector velocity, double mass) {
		super(fileName,scale, velocity);
		this.mass = mass;
	}
	
	public MasObject3D(String fileName, float scale, double mass) {
		super(fileName,scale);
		this.mass = mass;
	}*/
	
	@Override
	public void addMass(double mass) {
		this.mass += mass;
	}

	@Override
	public void giveMass(double mass) {
		this.mass -= mass;
	}
	
	/*protected void calcAcceleration(SimpleVector force) {
		force.scalarMul((float)(1/mass)); 
		setAccelaration(force);
	}*/
	
	@Override
	public void calcAcceleration(SimpleVector force) {
		SimpleVector gravityField = new SimpleVector(force);
		gravityField.scalarMul((float)(1/mass));
		if(isGravity(gravityField)) {
			setAccelaration(gravityField);
		} else {
			setAccelaration(0,0,0);
		}
	}

	@Override
	public boolean isGravity(SimpleVector GravityField) {
		if(GravityField.length() < 0.5) {
			return false;
		}
		return true;
	}

}
