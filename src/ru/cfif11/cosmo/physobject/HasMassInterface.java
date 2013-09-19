package ru.cfif11.cosmo.physobject;

import com.threed.jpct.SimpleVector;

/**
 * Interface for any PhysObject3D having mass
 */
public interface HasMassInterface {

    /**
     * Adds weight to the PhysObject3D
     * @param mass the extra weight
     */
	public void addMass(double mass);

    /**
     * Takes away part of mass of the object PhysObject3D
     * @param mass the deductible weight
     */
	public void giveMass(double mass);

    /**
     * Attracted whether the object in this gravitational field?
     * @param GravityField the gravitational field
     * @return yes, if the field is strong or not
     */
	public boolean isGravity(SimpleVector GravityField);

}
