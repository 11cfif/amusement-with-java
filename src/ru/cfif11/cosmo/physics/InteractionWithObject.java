package ru.cfif11.cosmo.physics;

import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public abstract class InteractionWithObject implements Interaction {

	private InteractionType type;

	public InteractionWithObject(InteractionType type) {
		this.type = type;
	}

	public abstract void interactWithObject(PhysObject3D mainObject, PhysObject3D minorObject);

}
