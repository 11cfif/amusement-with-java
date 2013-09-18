package ru.cfif11.cosmo.physobject;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

public class MassAttractObject3D extends MassObject3D implements CanAttractInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2055772860845712440L;

	static public MassAttractObject3D load3DS (String filename, float scale, SimpleVector velocity, double mass) {
		MassAttractObject3D obj =  (MassAttractObject3D)Loader.load3DS(filename, scale)[0];
		obj.setVelocity(velocity);
		obj.setMass(mass);
		return obj;
	}
	
	static public MassAttractObject3D getSphere (int faces, float scale, SimpleVector velocity, double mass) {
		MassAttractObject3D obj =  (MassAttractObject3D)Primitives.getSphere(faces, scale);
		obj.setVelocity(velocity);
		obj.setMass(mass);
		return obj;
	}

	public MassAttractObject3D(Object3D obj, SimpleVector velocity, double mass) {
		super(obj, velocity, mass);
	}

	@Override
	public double calcGravityField(SimpleVector vec) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double calcGravityField(float length) {
		// TODO Auto-generated method stub
		return 0;
	}

}
