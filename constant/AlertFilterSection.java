package com.se2.alert.constant;

public enum AlertFilterSection {


	FLAGGED(1), ALERT_TYPE(2), MATCH_TYPE(3), MANUFACTURER(4), SCOPE(5) ,PERMISSION(6) ,DATERANGE(7), STATUS(8), PRIORITY(9), ASSIGNEES(10), SEARCHKEYWORD(11),CREATED_BY(12),SETTING_ID(13),PCN_ID(14),DASHBOARD_ID(15);

	int value;

	AlertFilterSection(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
