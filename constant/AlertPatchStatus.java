package com.se2.alert.constant;

public enum AlertPatchStatus {

	WAITING(1), IN_PROGRESS(2), SUCCEED(3), FAILED(4);

	private int value;

	AlertPatchStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
