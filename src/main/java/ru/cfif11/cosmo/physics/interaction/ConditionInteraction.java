package ru.cfif11.cosmo.physics.interaction;

import java.util.*;

import ru.cfif11.cosmo.object.physobject.Field;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public abstract class ConditionInteraction {

	private static final Map<String, ConditionInteraction> cache =
		Collections.synchronizedMap(new HashMap<String, ConditionInteraction>());

	public static ConditionInteraction getConditionInteraction(String name) {
		return cache.get(name);
	}

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
		boolean result = false;
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
		if (cache.containsKey(name))
			throw new IllegalStateException("ConditionInteraction with this name already exists.");
		if (name == null)
			throw new IllegalStateException("name can never be null.");
		this.name = name;
		this.description = description;
		cache.put(name, this);
	}

	public abstract boolean isConsiderInteraction(PhysObject3D mainObject, PhysObject3D minorObject);

	public abstract boolean isConsiderInteraction(Field field, PhysObject3D minorObject);

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "ConditionInteraction " + name + ", description: " + description;
	}
}