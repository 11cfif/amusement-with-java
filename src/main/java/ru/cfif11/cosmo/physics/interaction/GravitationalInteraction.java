package ru.cfif11.cosmo.physics.interaction;

import java.util.Arrays;
import java.util.List;

import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.object.physobject.*;
import ru.cfif11.cosmo.physics.PhysConstant;

class GravitationalInteraction extends Interaction {

	public static final String NAME = "gravity";

	GravitationalInteraction(List<ConditionInteraction> conditions) {
		super(NAME, Arrays.asList(InteractionType.GRAVITATIONAL), conditions);
	}

	//private DescriberGravity() {	}

	@Override
	public void describe(PhysObject3D mainObject, PhysObject3D minorObject) {
		SimpleVector direction = minorObject.getTransformedCenter().calcSub(mainObject.getTransformedCenter());
		double distance = direction.length();
		direction.scalarMul((float)(PhysConstant.GRAVITATIONAL_CONSTANT.value() * minorObject.getMass() /
			(distance * distance * distance)));
		mainObject.setAcceleration(direction.calcAdd(mainObject.getAcceleration()));
	}

	@Override
	public void describe(Field field, PhysObject3D minorObject) {
		minorObject.setAcceleration(minorObject.getAcceleration()
			.calcAdd(field.getCharacteristic(GravityFieldOfEarth.FREE_FALL_ACCELERATION_V, SimpleVector.class)));
	}
}
