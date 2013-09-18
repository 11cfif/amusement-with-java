package ru.cfif11.cosmo.physobject;

import com.threed.jpct.SimpleVector;

public interface CanAttractInterface extends HasMassInterface {
	
	public double calcGravityField(SimpleVector vec);
	public double calcGravityField(float length);

}
