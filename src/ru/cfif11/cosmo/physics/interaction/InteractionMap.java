package ru.cfif11.cosmo.physics.interaction;

import java.util.*;

import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public class InteractionMap {

	private final HashMap<InteractionType, Set<PhysObject3D>> objectsByInteractionType = new HashMap<>();

	public InteractionMap(List<PhysObject3D> objects) {
		for (PhysObject3D object : objects) {
			processingPhysObjectInteraction(object);
		}
	}

	public Set<PhysObject3D> getInteractingObjects(PhysObject3D mainObject, InteractionType type) {
		Set<PhysObject3D> listInteractingObjects = new HashSet<>(objectsByInteractionType.get(type));
		listInteractingObjects.remove(mainObject);
		return listInteractingObjects;
	}

	public void addPhysObject(PhysObject3D object) {
		processingPhysObjectInteraction(object);
	}

	public void processingPhysObjectInteraction(PhysObject3D object) {
		for (InteractionType type : object.getInteractionTypes()) {
			if(objectsByInteractionType.keySet().contains(type))
				objectsByInteractionType.get(type).add(object);
			else
				objectsByInteractionType.put(type, new HashSet<>(Collections.singletonList(object)));
		}
	}
}
