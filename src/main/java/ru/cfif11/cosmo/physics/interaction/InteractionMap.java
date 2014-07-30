package ru.cfif11.cosmo.physics.interaction;

import java.util.*;

import ru.cfif11.cosmo.object.physobject.Field;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public class InteractionMap {

	private final HashMap<InteractionType, Set<PhysObject3D>> objectsByInteractionType = new HashMap<>();
	private final HashMap<InteractionType, Set<Field>> fieldsByInteractionType = new HashMap<>();

	public InteractionMap(List<PhysObject3D> objects, List<Field> fields) {
		if (objects != null) {
			for (PhysObject3D object : objects)
				processingPhysObjectInteraction(object);
		}
		if (fields == null)
			return;
		for (Field field : fields)
			processingFieldsInteraction(field);
	}

	public Set<PhysObject3D> getInteractingObjects(PhysObject3D mainObject, InteractionType type) {
		Set<PhysObject3D> listInteractingObjects = new HashSet<>(objectsByInteractionType.get(type));
		listInteractingObjects.remove(mainObject);
		return listInteractingObjects;
	}

	public Set<Field> getInteractingFileds(InteractionType type) {
		return fieldsByInteractionType.get(type);
	}

	public void addPhysObject(PhysObject3D object) {
		processingPhysObjectInteraction(object);
	}

	public void addField(Field field) {
		processingFieldsInteraction(field);
	}

	public void processingPhysObjectInteraction(PhysObject3D object) {
		for (InteractionType type : object.getInteractionTypes()) {
			if(objectsByInteractionType.keySet().contains(type))
				objectsByInteractionType.get(type).add(object);
			else
				objectsByInteractionType.put(type, new HashSet<>(Arrays.asList(object)));
		}
	}

	private void processingFieldsInteraction(Field field) {
		InteractionType type = field.getInteractionType();
		if(fieldsByInteractionType.keySet().contains(type))
			fieldsByInteractionType.get(type).add(field);
		else
			fieldsByInteractionType.put(type, new HashSet<>(Arrays.asList(field)));
	}
}
