package com.se2.alert.constant;

public enum AlertRunStatus {

	IN_PROGRESS(1), FINISHED(2), EXCEPTION_IN_PROCESS(3), SOME_EMAILS_FAILED(4);

	private int value;

	AlertRunStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
