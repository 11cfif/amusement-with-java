package ru.cfif11.cosmo.physics;

import java.util.*;

import ru.cfif11.cosmo.scene.Location;
import ru.cfif11.cosmo.object.physobject.PhysObject3D;

public class PhysEngine {

	private EnumMap<InteractionType, Set<PhysObject3D>> typesSetsPhysObject = new EnumMap<>(InteractionType.class);

	public PhysEngine(Location location) {
		init(location);
	}


	public void init(Location location) {
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

	public void calculate(float dT) {
		for (Map.Entry<InteractionType, Set<PhysObject3D>> typeSetsEntry : typesSetsPhysObject.entrySet()) {
			for (PhysObject3D mainObj : typeSetsEntry.getValue()) {
				mainObj.calculate(typeSetsEntry, dT);
			}
		}
	}

}
