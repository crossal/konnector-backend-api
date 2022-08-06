package com.konnector.backendapi.notification;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
	CONNECTION_REQUEST, CONNECTION_ACCEPT;

	@JsonValue
	public int toValue() {
		return ordinal();
	}
}
