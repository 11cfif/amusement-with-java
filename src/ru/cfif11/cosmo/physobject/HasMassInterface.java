package ru.cfif11.cosmo.physobject;

import com.threed.jpct.SimpleVector;

public interface HasMassInterface {
	
	public void addMass(double mass);
	
	public void giveMass(double mass);
	
	public boolean isGravity(SimpleVector GravityField);

}
