package ru.cfif11.cosmo.physics.interaction;

import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.physics.*;

class GravityInteraction extends DescriberInteraction {

	static final GravityInteraction INSTANCE = new GravityInteraction();

	private GravityInteraction() {	}

	@Override
	public void describe(PhysObject3D mainObject, PhysObject3D minorObject) {
		SimpleVector direction = mainObject.getTransformedCenter().calcSub(minorObject.getTransformedCenter());
		double distance = direction.length();
		direction.scalarMul((float)(PhysConstantEnum.GRAVITATIONAL_CONSTANT.value() * minorObject.getMass() /
			(distance * distance * distance)));
		minorObject.setAcceleration(direction.calcAdd(minorObject.getAcceleration()));
	}

	@Override
	public void describe(Field field, PhysObject3D minorObject) {
		minorObject.setAcceleration(minorObject.getAcceleration()
			.calcAdd(field.getCharacteristic(GravitationalFieldOfEarth.FREE_FALL_ACCELERATION_V, SimpleVector.class)));
	}

	@Override
	public String toString() {
		return "Describer gravity interaction";
	}
}
