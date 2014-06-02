package ru.cfif11.cosmo.locations.enumeration;

import java.util.ArrayList;
import java.util.List;

import ru.cfif11.cosmo.object.physobject.Medium;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;
import ru.cfif11.cosmo.physics.PhysEngine;

public class Location {

	private final List<PhysObject3D> objects = new ArrayList<>();
	private final List<Medium> mediums = new ArrayList<>();

	private PhysEngine engine;

	public Location(List<PhysObject3D> objects, List<Medium> mediums) {
		this.objects.addAll(objects);
		this.mediums.addAll(mediums);
		this.engine = new PhysEngine(this);
		initLocation();
	}

	public List<PhysObject3D> getObjects() {
		return objects;
	}

	public List<Medium> getMediums() {
		return mediums;
	}

	public void addPhysObject(PhysObject3D physObject3D) {
		engine.addPhysObject(physObject3D);
	}

	private void initLocation() {
		engine.init();
	}
}
