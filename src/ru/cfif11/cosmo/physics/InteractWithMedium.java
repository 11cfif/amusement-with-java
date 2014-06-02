package ru.cfif11.cosmo.physics;

import ru.cfif11.cosmo.object.physobject.Medium;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public interface InteractWithMedium extends Interaction {

	public void interactWithMedium(PhysObject3D mainObject, Medium medium);

}
