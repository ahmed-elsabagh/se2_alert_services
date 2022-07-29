package com.se2.alert.constant;


public enum UserType {

	MASTER_ADMIN(1), SALES(2), CS(3), QUALITY(4), COMPANY_ADMIN(5), SITE_ADMIN(6), REGULAR(7), DATA_TEAM(8),
	DEMO_USER(9), MASTER_ADMIN_READ_ONLY(10), INSIDE_SALES(11), ALERT_OPERATOR(12), ACL_DATA_OPERATOR(13),
	SALES_READ_ONLY(14);

	private int value;

	UserType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

}