package ru.cfif11.cosmo.scene;

import java.util.*;

import ru.cfif11.cosmo.object.physobject.Field;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.physics.interaction.InteractionMap;
import ru.cfif11.cosmo.physics.interaction.InteractionType;

public class Location {

	private final List<PhysObject3D> objects = new ArrayList<>();
	private final List<Field> fields = new ArrayList<>();
	private PhysObject3D selectObject = null;
	private InteractionMap interactionMap;


	public Location(List<PhysObject3D> objects, List<Field> fields) {
		this.objects.addAll(objects);
		interactionMap = new InteractionMap(objects, fields);
		if (fields != null)
			this.fields.addAll(fields);
	}

	public List<PhysObject3D> getObjects() {
		return objects;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void addPhysObject(PhysObject3D physObject3D) {
		objects.add(physObject3D);
		interactionMap.addPhysObject(physObject3D);
	}

	public void addField(Field field) {
		fields.add(field);
		interactionMap.addField(field);
	}

	@SuppressWarnings("unchecked")
	public Set<PhysObject3D> getInteractingObjects(PhysObject3D mainObject, InteractionType type) {
		Set<PhysObject3D> result = interactionMap.getInteractingObjects(mainObject, type);
		if (result == null)
			return Collections.EMPTY_SET;
		return result;
	}

	@SuppressWarnings("unchecked")
	public Set<Field> getInteractingFields(InteractionType type) {
		Set<Field> result = interactionMap.getInteractingFileds(type);
		if (result == null)
			return Collections.EMPTY_SET;
		return result;
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
