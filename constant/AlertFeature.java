package com.se2.alert.constant;

import java.util.HashMap;
import java.util.Map;

public enum AlertFeature {

	PCN((short) 1), Lifecycle((short) 2), DataSheet((short) 3), ROHS((short) 4), REACH((short) 5), ManufacturerAcquisitionChanges(
			(short) 6), GIDEP((short) 7),  SUPPLYCHAIN((short)9),  MARKETAVAILABILITY((short)10);



	short value;  

	AlertFeature(short value) {
		this.value = value;
	}

	public short getValue() {
		return this.value;
	}

	private static Map<Short, AlertFeature> map = new HashMap<>();

	static {
		for (AlertFeature alertFeature : AlertFeature.values())
			map.put(alertFeature.value, alertFeature);
	}

	public static AlertFeature valueOf(short alertFeature) {
		return map.get(alertFeature);
	}
}
