package com.konnector.backendapi.verification;

public enum VerificationStatus {
	INCOMPLETE(0), COMPLETE(1);

	private int value;

	VerificationStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public static VerificationStatus fromInt(int value) {
		for (VerificationStatus e : VerificationStatus.values()) {
			if (e.value == value) {
				return e;
			}
		}
		return null;
	}
}
