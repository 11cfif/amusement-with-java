package ru.cfif11.cosmo.physics;

import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.physics.interaction.InteractionType;
import ru.cfif11.cosmo.physics.interaction.InteractionWithObject;
import ru.cfif11.cosmo.scene.Location;

public class PhysEngine {

	private final Location location;

	public PhysEngine(Location location) {
		this.location = location;
	}

	public void calculate(float dT) {
		System.out.println("----------- START -----------");
		for (PhysObject3D mainObj : location.getObjects()) {
			mainObj.setAcceleration(SimpleVector.ORIGIN);
			for (InteractionType interactionType : mainObj.getInteractionTypes()) {
				for (PhysObject3D minorObj : location.getInteractingObjects(mainObj, interactionType)) {
					for (InteractionWithObject interactionWithObject : mainObj.getInteraction(interactionType)) {
						System.out.println("MainObj = " + mainObj + ", minorObj = " + minorObj);

						System.out.println("interactionWithObject = " + interactionWithObject);
						interactionWithObject.interactWithObject(mainObj, minorObj);
					}
				}
			}
			mainObj.calcLocation(dT);
		}
		System.out.println("----------- END -----------");
	}

}
