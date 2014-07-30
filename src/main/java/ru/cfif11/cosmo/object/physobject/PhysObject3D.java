package ru.cfif11.cosmo.object.physobject;

import java.util.*;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import ru.cfif11.cosmo.object.SelectableInterface;
import ru.cfif11.cosmo.physics.interaction.Interaction;
import ru.cfif11.cosmo.physics.interaction.InteractionType;

/**
 * Abstract class PhysObject3D extends Object3D(jpct.lib).
 * It has physical velocity and acceleration parameters.
 */
public class PhysObject3D extends Object3D implements SelectableInterface {

	private static final long serialVersionUID = 2530831725509380538L;

	private SimpleVector velocity;
	private SimpleVector acceleration = new SimpleVector();

	private double mass;
	private double charge;
	private double temperature;
	private int[] characteristicSizes;
	private boolean select = false;

	private Set<InteractionType> interactionTypes;

	private List<Interaction> interactions = new ArrayList<>();

	//========================== static Common API ==========================

	public static Builder newBuilder(Object3D object3D, String name, InteractionType... types) {
		return new Builder(object3D, name, types);
	}

	public static PhysObject3D createPhysObject3D(Object3D object3D, String name, InteractionType... types) {
		return newBuilder(object3D, name, types).build();
	}

	//========================== Common API ==========================

	@Override
	public boolean isSelect() {
		return select;
	}

	@Override
	public void setSelect(boolean select) {
		this.select = select;
	}

	public int[] getCharacteristicSizes() {
		return characteristicSizes;
	}

	public SimpleVector getVelocity() {
		return velocity;
	}

	public SimpleVector getAcceleration() {
		return acceleration;
	}

	public Set<InteractionType> getInteractionTypes() {
		return interactionTypes;
	}

	public double getMass() {
		return mass;
	}

	public double getCharge() {
		return charge;
	}

	public double getTemperature() {
		return temperature;
	}

	public void addInteraction(Interaction interaction) {
		this.interactions.add(interaction);
	}

	public List<Interaction> getInteractions() {
		return interactions;
	}

	public void removeInteraction(Interaction interaction) {
		this.interactions.remove(interaction);
	}

	/**
	 * Calculates the new location of the PhysObject3D to a new step
	 *
	 * @param dT the time step
	 */
	public void calcLocation(float dT) {
		calcVelocity(dT);
		SimpleVector dLocation = new SimpleVector(this.velocity);
		dLocation.scalarMul(dT);
		translate(dLocation);
	}

	/**
	 * Sets the velocity of the PhysObject3D
	 *
	 * @param velocity the velocity vector
	 */
	public void setVelocity(SimpleVector velocity) {
		this.velocity = velocity;
	}

	/**
	 * Sets the velocity of the PhysObject3D based on the three parameters
	 *
	 * @param vx the X component of the velocity
	 * @param vy the Y component of the velocity
	 * @param vz the Z component of the velocity
	 */
	public void setVelocity(float vx, float vy, float vz) {
		this.velocity.set(vx, vy, vz);
	}

	public void setAcceleration(SimpleVector acceleration) {
		this.acceleration = acceleration;
	}

	public boolean isConsiderInteraction(InteractionType key) {
		return true;
	}

	@Override
	public String toString() {
		return "PhysObj " + getName() +
			"( pos: " + getTransformedCenter() +
			", vel:" + velocity +
			", accel:" + acceleration +
			", mass:" + mass +
			", charge:" + charge +
			", temperature:" + temperature + ")";
	}

	//========================== private implementation ==========================

	private PhysObject3D(Object3D obj, String name, SimpleVector velocity, int[] sizes, Set<InteractionType> types,
		double mass, double charge, double temperature)
	{
		super(obj);
		setName(name);
		this.velocity = velocity;
		this.acceleration = new SimpleVector();
		this.velocity = velocity;
		this.characteristicSizes = sizes;
		this.interactionTypes = types;
		this.mass = mass;
		this.charge = charge;
		this.temperature = temperature;
	}

	private void calcVelocity(float dT) {
		SimpleVector dVelocity = new SimpleVector(this.acceleration);
		dVelocity.scalarMul(dT);
		this.velocity.add(dVelocity);
	}

	public List<Interaction> getInteraction(InteractionType key) {
		List<Interaction> interWithObjects = new ArrayList<>();
		for (Interaction interaction : interactions) {
			if( interaction.hasInteractionType(key))
				interWithObjects.add(interaction);
		}
		return interWithObjects;
	}


	//========================== Builder ==========================

	public static class Builder {

		private Object3D object3D;
		private String name;
		private SimpleVector velocity;
		private double mass;
		private double charge;
		private double temperature;
		private int[] characteristicSizes;
		private Set<InteractionType> types = new HashSet<>();

		Builder(Object3D object3D, String name, InteractionType... types) {
			this.object3D = object3D;
			this.name = name;
			if(types != null)
				this.types.addAll(Arrays.asList(types));
		}

		public final Builder withMass(double mass) {
			this.mass = mass;
			return this;
		}

		public final Builder withInteractionTypes(InteractionType... types) {
			if(types != null)
				this.types.addAll(Arrays.asList(types));
			return this;
		}

		public final Builder withCharge(double charge) {
			this.charge = charge;
			return this;
		}

		public final Builder withTemperature(double temperature) {
			this.temperature = temperature;
			return this;
		}

		public final Builder withVelocity(SimpleVector velocity) {
			this.velocity = velocity;
			return this;
		}

		public final Builder withInitialPos(SimpleVector position) {
			object3D.setOrigin(position);
			return this;
		}

		public final Builder withCharacteristicSize(int[] sizes) {
			this.characteristicSizes = sizes;
			return this;
		}

		public PhysObject3D build() {
			return new PhysObject3D(object3D, name, velocity != null ? velocity : new SimpleVector(),
				characteristicSizes, types, mass, charge, temperature);
		}

	}

}
