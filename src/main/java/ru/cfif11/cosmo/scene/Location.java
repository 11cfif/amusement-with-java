package ru.cfif11.cosmo.scene;

import java.util.*;

import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.physics.interaction.InteractionMap;
import ru.cfif11.cosmo.physics.interaction.InteractionType;

public class Location {

	private final List<PhysObject3D> objects = new ArrayList<>();
	private PhysObject3D selectObject = null;
	private InteractionMap interactionMap;


	public Location(List<PhysObject3D> objects) {
		this.objects.addAll(objects);
		interactionMap = new InteractionMap(objects);
	}

	public List<PhysObject3D> getObjects() {
		return objects;
	}

	public void addPhysObject(PhysObject3D physObject3D) {
		objects.add(physObject3D);
		interactionMap.addPhysObject(physObject3D);
	}

	public Set<PhysObject3D> getInteractingObjects(PhysObject3D mainObject, InteractionType type) {
		return interactionMap.getInteractingObjects(mainObject, type);
	}

	public PhysObject3D getSelectObject() {
		return selectObject;
	}

	public void setSelectObject(PhysObject3D selectObject) {
		this.selectObject = selectObject;
	}

	@Override
	public String toString() {
		return objects.toString();
	}
}
