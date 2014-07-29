package ru.cfif11.cosmo.physics.interaction;

import java.util.Arrays;
import java.util.List;

import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.object.physobject.*;
import ru.cfif11.cosmo.physics.PhysConstant;

class DescriberGravity extends Interaction {

	public static final String NAME = "gravity";

	DescriberGravity(List<ConditionInteraction> conditions) {
		super(NAME, Arrays.asList(InteractionType.GRAVITATIONAL), conditions);
	}

	//private DescriberGravity() {	}

	@Override
	public void describe(PhysObject3D mainObject, PhysObject3D minorObject) {
		SimpleVector direction = mainObject.getTransformedCenter().calcSub(minorObject.getTransformedCenter());
		double distance = direction.length();
		direction.scalarMul((float)(PhysConstant.GRAVITATIONAL_CONSTANT.value() * mainObject.getMass() /
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
