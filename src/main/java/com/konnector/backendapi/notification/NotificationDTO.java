package com.konnector.backendapi.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class NotificationDTO {

	private Long id;
	private Long recipientId;
	private Long senderId;
	private NotificationType type;
	private Long referenceId;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime createdOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NotificationDTO that = (NotificationDTO) o;
		return Objects.equals(id, that.id) && Objects.equals(recipientId, that.recipientId) && Objects.equals(senderId, that.senderId) && type == that.type && Objects.equals(referenceId, that.referenceId) && Objects.equals(createdOn, that.createdOn);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, recipientId, senderId, type, referenceId, createdOn);
	}
}
