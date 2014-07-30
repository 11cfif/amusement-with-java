package ru.cfif11.cosmo.physics.interaction;

import java.util.*;

import ru.cfif11.cosmo.object.physobject.Field;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public abstract class ConditionInteraction {

	public static boolean interactIsConsidered(
		Set<ConditionInteraction> conditions, PhysObject3D mainObj, PhysObject3D minorObj)
	{
		boolean result = true;
		for (ConditionInteraction condition : conditions) {
			result = condition.isConsiderInteraction(mainObj, minorObj);
			if(!result)
				break;
		}
		return result;
	}

	public static boolean interactIsConsidered(Set<ConditionInteraction> conditions, Field field, PhysObject3D minorObj)
	{
		boolean result = true;
		for (ConditionInteraction condition : conditions) {
			result = condition.isConsiderInteraction(field, minorObj);
			if(!result)
				break;
		}
		return result;
	}

	private final String name;
	private final String description;

	protected ConditionInteraction(String name, String description) {
		if (name == null)
			throw new IllegalStateException("name can never be null.");
		this.name = name;
		this.description = description;
	}

	public abstract boolean isConsiderInteraction(PhysObject3D mainObject, PhysObject3D minorObject);

	public abstract boolean isConsiderInteraction(Field field, PhysObject3D minorObject);

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "ConditionInteraction " + name + ", description: " + description;
	}
}
