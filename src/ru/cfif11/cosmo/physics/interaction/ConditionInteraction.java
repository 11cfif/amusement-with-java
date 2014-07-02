package ru.cfif11.cosmo.physics.interaction;

import java.util.*;

import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public abstract class ConditionInteraction {

	private static final Map<String, ConditionInteraction> cache =
		Collections.synchronizedMap(new HashMap<String, ConditionInteraction>());

	public static ConditionInteraction getConditionInteraction(String name) {
		return cache.get(name);
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

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "ConditionInteraction " + name + ", description: " + description;
	}
}
