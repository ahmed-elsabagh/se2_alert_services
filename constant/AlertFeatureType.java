package com.se2.alert.constant;

import java.util.HashMap;
import java.util.Map;

public enum AlertFeatureType {
	INSTOCK((short) 47), OUTSTOCK((short) 46), STOCKABOVE((short) 48), STOCKBELOW((short) 49);

	short value;  

	AlertFeatureType(short value) {
		this.value = value;
	}

	public short getValue() {
		return this.value;
	}

	private static Map<Short, AlertFeatureType> map = new HashMap<>();

	static {
		for (AlertFeatureType alertFeature : AlertFeatureType.values())
			map.put(alertFeature.value, alertFeature);
	}

	public static AlertFeatureType valueOf(short alertFeature) {
		return map.get(alertFeature);
	}

}