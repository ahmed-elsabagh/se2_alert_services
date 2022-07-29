package com.se2.alert.constant;

import java.util.HashMap;
import java.util.Map;

public enum AlertScope {
	BOM(0), PROJECT(1), ACL(2), PART(3);

	int value;

	AlertScope(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	private static Map<Integer, AlertScope> map = new HashMap<>();

	static {
		for (AlertScope alertScope : AlertScope.values())
			map.put(alertScope.value, alertScope);
	}

	public static AlertScope valueOf(int alertScope) {
		return map.get(alertScope);
	}
}