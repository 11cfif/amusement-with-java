package ru.cfif11.cosmo.physics.interaction;

import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.physics.Field;

public abstract class DescriberInteraction {

	public static final DescriberInteraction GRAVITY_INTERACTION = GravityInteraction.INSTANCE;

	public abstract void describe(PhysObject3D mainObject, PhysObject3D minorObject);

	public abstract void describe(Field field, PhysObject3D minorObject);

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * This implementation return true when the other object's class is equal to this one.
	 * @return  {@code true} if this object is the same as the obj
	 *          argument; {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		return obj != null && getClass().equals(obj.getClass());
	}

	/**
	 * Returns a hash code value for the object.
	 * This implementation return hashCode of this object's class name.
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return getClass().getName().hashCode();
	}
}
