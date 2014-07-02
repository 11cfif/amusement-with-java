package ru.cfif11.cosmo.physics.interaction;

import java.util.*;

import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public class InteractionWithObject {

	private final List<InteractionType> types = new ArrayList<>();
	private final List<ConditionInteraction> conditions = new ArrayList<>();
	private final String name;
	private final String key;
	private final DescriberInteraction describerInteraction;

	private static final Map<String, InteractionWithObject> cache =
		Collections.synchronizedMap(new HashMap<String, InteractionWithObject>());

	public static InteractionWithObject valueOf(String name, InteractionType type,
		DescriberInteraction describerInteraction, ConditionInteraction... conditions)
	{
		if(type == null)
			throw new IllegalStateException("Type can never be null.");
		return valueOf(name, Arrays.asList(type), describerInteraction, conditions);
	}

	public static InteractionWithObject valueOf(String name, InteractionType[] types,
		DescriberInteraction describerInteraction, ConditionInteraction... conditions)
	{
		if(types == null)
			throw new IllegalStateException("Type can never be null.");
		return valueOf(name, Arrays.asList(types), describerInteraction, conditions);
	}

	public static InteractionWithObject valueOf(String name, List<InteractionType> types,
		DescriberInteraction describerInteraction, ConditionInteraction... conditions)
	{
		if(name == null)
			throw new IllegalStateException("Name can never be null.");
		if(types == null)
			throw new IllegalStateException("Types can never be null.");
		if(types.isEmpty())
			throw new IllegalStateException("Type can never be empty.");
		if(describerInteraction == null)
			throw new IllegalStateException("DescriberInteraction can never be null.");
		String key = generatedKey(name, types, describerInteraction.toString(), conditions);
		if(cache.containsKey(key))
			return cache.get(key);
		return new InteractionWithObject(name, types, describerInteraction, key, conditions);
	}

	private static String generatedKey(String name, List<InteractionType> types, String describer,
		ConditionInteraction... conditionsNames)
	{
		String result = "InteractionWithObject " + name + " = (InteractionTypes: ";
		for (InteractionType type : types)
			result += type + ", ";
		result += "describer: " + describer + ", ";
		if(conditionsNames.length != 0) {
			result += "conditionsName: ";
			for (ConditionInteraction condition : conditionsNames)
				result += condition.getName() + ", ";
		}
		result = result.substring(0, result.length() - 2);
		return result;
	}

	private InteractionWithObject(String name, List<InteractionType> types, DescriberInteraction describerInteraction,
		String key, ConditionInteraction... conditions)
	{
		if (cache.containsKey(name))
			throw new IllegalStateException("InteractionWithObject with this name already exists");
		if (name == null)
			throw new IllegalStateException("name can never be null.");
		this.name = name;
		this.types.addAll(types);
		this.conditions.addAll(Arrays.asList(conditions));
		this.describerInteraction = describerInteraction;
		this.key = key;
		cache.put(key, this);
	}

	public List<InteractionType> getInteractionTypes() {
		return types;
	}

	public boolean hasInteractionType(InteractionType key) {
		return types.contains(key);
	}

	public void interactWithObject(PhysObject3D mainObject, PhysObject3D minorObject) {
		if(!conditions.isEmpty()) {
			for (ConditionInteraction condition : conditions) {
				if (condition != null && condition.isConsiderInteraction(mainObject, minorObject))
					describerInteraction.describe(mainObject, minorObject);
			}
		} else {
			describerInteraction.describe(mainObject, minorObject);
		}
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof InteractionWithObject) && obj.toString().equals(key);
	}

	@Override
	public String toString() {
		return key;
	}
}
