package ru.cfif11.cosmo.physics.interaction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import ru.cfif11.cosmo.object.physobject.Field;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public abstract class Interaction {

	public static final Map<String, Class<? extends Interaction>> interactionClasses = new HashMap<>();
	private static final Map<String, Interaction> cache = Collections.synchronizedMap(new HashMap<String, Interaction>());

	static {
		interactionClasses.put(DescriberGravity.NAME, DescriberGravity.class);
	}

	private final List<InteractionType> types = new ArrayList<>();
	private final Set<ConditionInteraction> conditions = new HashSet<>();
	private String key;


	public static Interaction getInteraction(String name, ConditionInteraction[] conditions) {
		if (!interactionClasses.containsKey(name))
			throw new IllegalArgumentException("Interaction with the name " + name +  " does not exist.");
		if(conditions == null)
			conditions = new ConditionInteraction[0];
		String key = generatedKey(name, Arrays.asList(conditions));
		Interaction result = cache.get(key);
		if (result == null) {
			try {
				Constructor<? extends Interaction> constructor = interactionClasses.get(name).getDeclaredConstructor( List.class);
				result = constructor.newInstance(Arrays.asList(conditions));
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		cache.put(key, result);
		return result;
	}

	private static String generatedKey(String name, List<ConditionInteraction> conditionsNames) {
		String result = "Interaction " + name;
		if(conditionsNames != null) {
			result += "conditionsName: ";
			for (ConditionInteraction condition : conditionsNames)
				result += condition.getName() + ", ";
			result = result.substring(0, result.length() - 2);
		}
		return result;
	}

	protected Interaction(String name, List<InteractionType> types, List<ConditionInteraction> conditions)
	{
		this.types.addAll(types);
		this.conditions.addAll(conditions);
		this.key = generatedKey(name, conditions);
	}

	public List<InteractionType> getInteractionTypes() {
		return types;
	}

	public boolean hasInteractionType(InteractionType key) {
		return types.contains(key);
	}

	public void interactWithObject(PhysObject3D mainObj, PhysObject3D minorObj) {
		if(ConditionInteraction.interactIsConsidered(conditions, mainObj, minorObj))
			describe(mainObj, minorObj);
	}

	public void interactWithField(Field field, PhysObject3D minorObj) {
		if(ConditionInteraction.interactIsConsidered(conditions, field, minorObj))
			describe(field, minorObj);
	}

	public abstract void describe(PhysObject3D mainObject, PhysObject3D minorObject);

	public abstract void describe(Field field, PhysObject3D minorObject);

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Interaction) && obj.toString().equals(key);
	}

	@Override
	public String toString() {
		return key;
	}
}
