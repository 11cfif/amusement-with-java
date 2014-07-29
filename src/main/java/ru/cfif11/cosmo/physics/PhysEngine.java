package ru.cfif11.cosmo.physics;

import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.physics.interaction.Interaction;
import ru.cfif11.cosmo.physics.interaction.InteractionType;
import ru.cfif11.cosmo.scene.Location;

public class PhysEngine {

	private final Location location;

	public Location getLocation() {
		return location;
	}

	public PhysEngine(Location location) {
		this.location = location;
	}

	public void calculate(float dT) {
		System.out.println("----------- START -----------");
		refresh();
		for (PhysObject3D mainObj : location.getObjects()) {
			for (InteractionType interactionType : mainObj.getInteractionTypes()) {
				for (PhysObject3D minorObj : location.getInteractingObjects(mainObj, interactionType)) {
					for (Interaction interaction : mainObj.getInteraction(interactionType)) {
						System.out.println("MainObj = " + mainObj + ", minorObj = " + minorObj);

						System.out.println("interactionWithObject = " + interaction);
						interaction.interactWithObject(mainObj, minorObj);
					}
				}
			}
			mainObj.calcLocation(dT);
		}
		System.out.println("----------- END -----------");
	}

	private void refresh() {
		for (PhysObject3D physObject3D : location.getObjects())
			physObject3D.setAcceleration(SimpleVector.ORIGIN);
	}
}
