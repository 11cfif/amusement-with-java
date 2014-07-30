package ru.cfif11.cosmo.physics;

import java.util.*;

import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;
import junit.framework.TestCase;
import ru.cfif11.cosmo.object.physobject.*;
import ru.cfif11.cosmo.physics.interaction.*;
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
		Location location = new Location(objects, null);
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

	public void testGravityWithField() {
		final double initialS = 100;
		PhysObject3D physObj = PhysObject3D.newBuilder(Primitives.getSphere(1),"boll", InteractionType.GRAVITATIONAL)
			.withMass(1)
			.withInitialPos(new SimpleVector(new SimpleVector(0, 0, initialS)))
			.build();
		physObj.addInteraction(Interaction.getInteraction("gravity", null));
		Location location = new Location(Arrays.asList(physObj), Arrays.asList((Field)new GravityFieldOfEarth()));
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
		System.out.println(9.7 * totalT);
		System.out.println(velocity);
		System.out.println(9.9 * totalT);
		assertTrue(velocity <= 9.9 * totalT);
		assertTrue(velocity >= 9.7 * totalT);

		float s = (physObj.getTransformedCenter().length());
		System.out.println("position:");
		System.out.println(initialS - 9.9 * totalT * totalT / 2);
		System.out.println(s);
		System.out.println(initialS - 9.7 * totalT * totalT / 2);
		assertTrue(s >= initialS - 9.9 * totalT * totalT / 2);
		assertTrue(s <= initialS - 9.7 * totalT * totalT / 2);
	}

	public void testCondition() {
		final double initialS = 40;
		PhysObject3D physObj = PhysObject3D.newBuilder(Primitives.getSphere(1),"boll", InteractionType.GRAVITATIONAL)
			.withMass(1)
			.withInitialPos(new SimpleVector(new SimpleVector(0, 0, initialS)))
			.build();
		physObj.addInteraction(Interaction.getInteraction("gravity",
			new ConditionInteraction[] {new SimpleConditionInteraction()}));
		Location location = new Location(Arrays.asList(physObj), Arrays.asList((Field)new GravityFieldOfEarth()));
		PhysEngine engine = new PhysEngine(location);
		float totalT = 4;
		float dT = 0.001f;
		float t = 0;
		double timeFall = 0;
		while(t <= totalT) {
			engine.calculate(dT);
			if(physObj.getTransformedCenter().z < 0 && timeFall == 0)
				timeFall = t;
			t += dT;
		}

		System.out.println("position:");
		System.out.println(Math.sqrt(2 * initialS / 10));
		System.out.println(timeFall);
		System.out.println(Math.sqrt(2 * initialS / 9.8));
		assertTrue(timeFall >= Math.sqrt(2 * initialS / 10));
		assertTrue(timeFall <= Math.sqrt(2 * initialS / 9.8));

		float s = (physObj.getTransformedCenter().length());
		System.out.println(s);
		assertTrue(Math.abs(s) < 0.02);
	}

	private static class SimpleConditionInteraction extends ConditionInteraction {
		private SimpleConditionInteraction() {
			super("simple", "gravity don't work on height less field.initPos");
		}

		@Override
		public boolean isConsiderInteraction(PhysObject3D mainObject, PhysObject3D minorObject) {
			return false;
		}

		@Override
		public boolean isConsiderInteraction(Field field, PhysObject3D minorObject) {
			SimpleVector temp = field.getCharacteristic(GravityFieldOfEarth.INITIAL_POSITION, SimpleVector.class);
			SimpleVector direct = field.getCharacteristic(GravityFieldOfEarth.DIRECTION_FORCE, SimpleVector.class);
			temp = minorObject.getTransformedCenter().calcSub(temp);
			float angle = temp.calcAngle(direct);
			System.out.println("angle = " + angle + "; PI/2 = " + Math.PI / 2);
			if (angle < Math.PI / 2) {
				minorObject.setVelocity(0, 0, 0);
				return false;
			}
			return true;
		}
	}

	public void testSolarSystem() {
		List<PhysObject3D> systems = new ArrayList<>();
		PhysObject3D physObj = PhysObject3D.newBuilder(Primitives.getSphere(1), "Solar", InteractionType.GRAVITATIONAL)
			.withMass(1.9891e30)
			.build();
		physObj.addInteraction(Interaction.getInteraction("gravity", null));
		systems.add(physObj);

		physObj = PhysObject3D.newBuilder(Primitives.getSphere(1),"Earth", InteractionType.GRAVITATIONAL)
			.withMass(PhysConstant.EARTH_MASS.value())
			.withInitialPos(new SimpleVector(149597870700d, 0, 0))
			.withVelocity(new SimpleVector(0, 29783, 0 ))
			.build();
		physObj.addInteraction(Interaction.getInteraction("gravity", null));
		systems.add(physObj);

		Location location = new Location(systems, null);
		PhysEngine engine = new PhysEngine(location);
		double totalT = 60 * 60 * 24 * 365;
		float dT = 1f;
		double t = 0;
		int k = 0;
		int day = 0;
		long time = System.currentTimeMillis();
		while (t <= totalT) {
			engine.calculate(dT);
			t += dT;
			if (k > 60 * 60 * 24) {
				System.out.println(day);
				System.out.println(t);
				k = 0;
				day++;
			}
			k++;
		}

		float velocity = physObj.getVelocity().length();
		System.out.println("velocity:");
		System.out.println(physObj.getVelocity());

		float s = physObj.getTransformedCenter().length();
		System.out.println("position:");
		System.out.println( physObj.getTransformedCenter());
		//todo:

	}
}
