package ru.cfif11.cosmo.physobject;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

public class MassObject3D extends PhysObject3D implements HasMassInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6141176990423946936L;
	private double mass;


	public MassObject3D(Object3D obj, SimpleVector velocity, double mass) {
		super(obj, velocity);
		this.mass = mass;
	}
	
	public double getMass() {
		return mass;
	}
	
	@Override
	public void addMass(double mass) {
		this.mass += mass;
	}

	@Override
	public void giveMass(double mass) {
		this.mass -= mass;
	}
	
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

    protected void setMass(double mass) {
        this.mass = mass;
    }

}
