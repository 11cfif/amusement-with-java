package ru.cfif11.cosmo.physobject;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

/**
 * This class describes the physical objects which have mass and can attract other MassObject3D
 */
public class MassAttractObject3D extends MassObject3D implements CanAttractInterface {

	private static final long serialVersionUID = -2055772860845712440L;

    /**
     * Create MassAttractObject3D on base MassObject3D, velocity and mass
     * @param obj the Object3D
     * @param velocity the velocity
     * @param mass the mass
     */
	public MassAttractObject3D(Object3D obj, String name, SimpleVector velocity, double mass) {
		super(obj, name, velocity, mass);
	}

}
