package com.konnector.backendapi;

public enum Environment {
	DEV("dev"),
	PROD("prod");

	private String value;

	Environment(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
