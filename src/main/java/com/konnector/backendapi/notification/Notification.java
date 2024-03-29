package com.konnector.backendapi.notification;

import com.konnector.backendapi.exceptions.InvalidDataException;
import com.konnector.backendapi.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "recipient_id")
	private Long recipientId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipient_id", insertable = false, updatable = false)
	private User recipient;

	@Column(name = "sender_id")
	private Long senderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", insertable = false, updatable = false)
	private User sender;

	@Column(name = "type")
	private NotificationType type;

	@Column(name = "reference_id")
	private Long referenceId;

	@Column(name = "created_on")
	@CreationTimestamp
	private LocalDateTime createdOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private LocalDateTime updatedOn;

	public Notification() {}

	public Notification(Long recipientId, Long senderId, NotificationType type, Long referenceId) {
		this.recipientId = recipientId;
		this.senderId = senderId;
		this.type = type;
		this.referenceId = referenceId;
	}

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

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
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

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void validateForCreation() {
		if (id != null) {
			throw new InvalidDataException("Id should be empty.");
		}

		validateForCreationAndUpdate();
	}

	public void validateForUpdate() {
		if (id == null) {
			throw new InvalidDataException("Id cannot be empty.");
		}

		validateForCreationAndUpdate();
	}

	private void validateForCreationAndUpdate() {
		if (recipientId == null) {
			throw new InvalidDataException("Recipient Id cannot be null.");
		}
		if (senderId == null) {
			throw new InvalidDataException("Sender Id cannot be null.");
		}
		if (recipientId.equals(senderId)) {
			throw new InvalidDataException("Recipient Id cannot be the same as sender Id.");
		}
		if (type == null) {
			throw new InvalidDataException("Type cannot be null.");
		}
		if (referenceId == null) {
			throw new InvalidDataException("Reference Id cannot be null.");
		}
	}
}
