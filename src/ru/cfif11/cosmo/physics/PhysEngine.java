package ru.cfif11.cosmo.physics;

import java.util.*;

import ru.cfif11.cosmo.locations.enumeration.Location;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public class PhysEngine {

	private final Location location;
	private EnumMap<InteractionType, Set<PhysObject3D>> typesSetsPhysObject = new EnumMap<>(InteractionType.class);

	public PhysEngine(Location location) {
		this.location = location;
	}


	public void init() {
		for (PhysObject3D physObject : location.getObjects()) {
			for (PhysObject3D.AppliedInteraction appliedType : physObject.getInteractionTypes()) {
				if(typesSetsPhysObject.containsKey(appliedType.getType()))
					typesSetsPhysObject.get(appliedType.getType()).add(physObject);
				else
					typesSetsPhysObject.put(appliedType.getType(), new HashSet<>(Collections.singleton(physObject)));
			}
		}
	}

	public void addPhysObject(PhysObject3D physObject) {
		for (PhysObject3D.AppliedInteraction appliedType : physObject.getInteractionTypes()) {
			if(typesSetsPhysObject.containsKey(appliedType.getType()))
				typesSetsPhysObject.get(appliedType.getType()).add(physObject);
			else
				typesSetsPhysObject.put(appliedType.getType(), new HashSet<>(Collections.singleton(physObject)));
		}
	}

	public void calculate() {
		for (Map.Entry<InteractionType, Set<PhysObject3D>> typeSetsEntry : typesSetsPhysObject.entrySet()) {
			for (PhysObject3D mainObj : typeSetsEntry.getValue()) {
				if(mainObj.isConsiderInteraction(typeSetsEntry.getKey())) {
					for(PhysObject3D minorObj : typeSetsEntry.getValue()) {
						for(InteractionWithObject interWithObj : mainObj.getInteraction(typeSetsEntry.getKey())) {
							interWithObj.interactWithObject(mainObj, minorObj);
							//todo: переделать взаимодействия обработку взаимодействий внутри объекта
						}
					}
				}

			}
		}
	}

}
