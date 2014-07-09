package ru.cfif11.cosmo.physics;

import java.util.HashMap;
import java.util.Map;

import ru.cfif11.cosmo.physics.interaction.InteractionType;

public class Field {

	private final InteractionType interactionType;
	private final Map<String, Object> characteristics = new HashMap<>();

	public Field(InteractionType interactionType) {
		this.interactionType = interactionType;
	}

	public Object addCharacteristic(String name, Object value) {
		return this.characteristics.put(name, value);
	}

	public Object getCharacteristic(String name) {
		return characteristics.get(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getCharacteristic(String name, Class<T> characteristicClass) {
		Object characteristic = characteristics.get(name);
		Class<?> resultType = characteristic.getClass();
		if (!resultType.isAssignableFrom(characteristicClass)) {
			throw new IllegalStateException("Incompatible result type: " + resultType.getName() +
				", expected: " + characteristicClass.getName());
		}
		return (T) characteristics.get(name);
	}

	public InteractionType getInteractionType() {
		return interactionType;
	}

}
