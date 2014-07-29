package ru.cfif11.cosmo.physics;

import java.util.ArrayList;
import java.util.List;

import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import junit.framework.TestCase;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.physics.interaction.Interaction;
import ru.cfif11.cosmo.physics.interaction.InteractionType;
import ru.cfif11.cosmo.scene.Location;

public class TestGravity extends TestCase {

	public void testGravityWithObject() {
		List<PhysObject3D> objects = new ArrayList<>();
		PhysObject3D physObj = PhysObject3D.newBuilder(Primitives.getSphere(1),"Earth", InteractionType.GRAVITATIONAL)
			.withMass(PhysConstant.EARTH_MASS.value())
			.build();
		physObj.addInteraction(Interaction.getInteraction("gravity", null));
		objects.add(physObj);

		final double initialS = 100;
		physObj = PhysObject3D.newBuilder(Primitives.getSphere(1),"boll", InteractionType.GRAVITATIONAL)
			.withMass(1)
			.withInitialPos(new SimpleVector(new SimpleVector(PhysConstant.EARTH_RADIUS.value() + initialS, 0,0)))
			.build();
		physObj.addInteraction(Interaction.getInteraction("gravity", null));
		objects.add(physObj);
		Location location = new Location(objects);
		PhysEngine engine = new PhysEngine(location);
		float totalT = 4;
		float dT = 0.005f;
		float t = 0;
		while(t <= totalT) {
			engine.calculate(dT);
			t += dT;
		}

		float velocity = physObj.getVelocity().length();
		System.out.println("velocity:");
		System.out.println(9.8 * totalT);
		System.out.println(velocity);
		System.out.println(10 * totalT);
		assertTrue(velocity <= 10 * totalT);
		assertTrue(velocity >= 9.8 * totalT);

		float s = (float)(physObj.getTransformedCenter().length() - PhysConstant.EARTH_RADIUS.value());
		System.out.println("position:");
		System.out.println(initialS - 10 * totalT * totalT / 2);
		System.out.println(s);
		System.out.println(initialS - 9.8 * totalT * totalT / 2);
		assertTrue(s >= initialS - 10 * totalT * totalT / 2);
		assertTrue(s <= initialS - 9.8 * totalT * totalT / 2);
	}
}
