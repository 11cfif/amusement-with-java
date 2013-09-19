package ru.cfif11.cosmo.physobject;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

public class MassAttractObject3D extends MassObject3D implements CanAttractInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2055772860845712440L;

	public MassAttractObject3D(Object3D obj, SimpleVector velocity, double mass) {
		super(obj, velocity, mass);
	}

}
