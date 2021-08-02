package com.konnector.backendapi.verification;

public enum VerificationType {
	EMAIL(0), PASSWORD(1);

	private int value;

	VerificationType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public static VerificationType fromInt(int value) {
		for (VerificationType e : VerificationType.values()) {
			if (e.value == value) {
				return e;
			}
		}
		return null;
	}
}
