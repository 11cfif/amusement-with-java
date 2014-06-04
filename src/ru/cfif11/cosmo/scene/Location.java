package ru.cfif11.cosmo.scene;

import java.util.ArrayList;
import java.util.List;

import ru.cfif11.cosmo.object.physobject.Medium;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public class Location {

	private final List<PhysObject3D> objects = new ArrayList<>();
	private final List<Medium> mediums = new ArrayList<>();
	private PhysObject3D selectObject = null;


	public Location(List<PhysObject3D> objects, List<Medium> mediums) {
		this.objects.addAll(objects);
		//this.mediums.addAll(mediums);
		init();
	}

	public List<PhysObject3D> getObjects() {
		return objects;
	}

	public List<Medium> getMediums() {
		return mediums;
	}

	public void addPhysObject(PhysObject3D physObject3D) {
		objects.add(physObject3D);
	}

	public void init() {

	}

	public PhysObject3D getSelectObject() {
		return selectObject;
	}

	public void setSelectObject(PhysObject3D selectObject) {
		this.selectObject = selectObject;
	}

	@Override
	public String toString() {
		return objects.toString();
	}
}
