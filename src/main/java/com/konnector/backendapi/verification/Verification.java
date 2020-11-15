package com.konnector.backendapi.verification;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Verification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "status")
	private VerificationStatus status;

	@Column(name = "code")
	private String code;

	@Column(name = "code_attempts_left")
	private int codeAttemptsLeft;

	@Column(name = "url_token")
	private String urlToken;

	@Column(name = "expiresOn")
	private LocalDateTime expiresOn;

	@Column(name = "created_on")
	@CreationTimestamp
	private LocalDateTime createdOn;

	@Column(name = "updated_on")
	@UpdateTimestamp
	private LocalDateTime updatedOn;

	public Verification(long userId, String code, int codeAttemptsLeft, String urlToken, LocalDateTime expiresOn) {
		this.userId = userId;
		this.status = VerificationStatus.INCOMPLETE;
		this.code = code;
		this.codeAttemptsLeft = codeAttemptsLeft;
		this.urlToken = urlToken;
		this.expiresOn = expiresOn;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public VerificationStatus getStatus() {
		return status;
	}

	public void setStatus(VerificationStatus status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCodeAttemptsLeft() {
		return codeAttemptsLeft;
	}

	public void setCodeAttemptsLeft(int codeAttemptsLeft) {
		this.codeAttemptsLeft = codeAttemptsLeft;
	}

	public String getUrlToken() {
		return urlToken;
	}

	public void setUrlToken(String urlToken) {
		this.urlToken = urlToken;
	}

	public LocalDateTime getExpiresOn() {
		return expiresOn;
	}

	public void setExpiresOn(LocalDateTime expiresOn) {
		this.expiresOn = expiresOn;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}
}