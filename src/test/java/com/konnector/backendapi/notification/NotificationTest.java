package com.konnector.backendapi.notification;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotificationTest {

	private final EasyRandom easyRandom = new EasyRandom();
	private final Notification notification = easyRandom.nextObject(Notification.class);


	@Test
	public void validateForCreation_idIsNotNull_throwsException() {
		ReflectionTestUtils.setField(notification, "id", 1L);
		assertThrows(InvalidDataException.class, () -> notification.validateForCreation());
	}

	@Test
	public void validateForCreation_recipientIdIsNull_throwsException() {
		ReflectionTestUtils.setField(notification, "id", null);
		notification.setRecipientId(null);
		assertThrows(InvalidDataException.class, () -> notification.validateForCreation());
	}

	@Test
	public void validateForCreation_senderIdIsNull_throwsException() {
		ReflectionTestUtils.setField(notification, "id", null);
		notification.setSenderId(null);
		assertThrows(InvalidDataException.class, () -> notification.validateForCreation());
	}

	@Test
	public void validateForCreation_senderIdEqualsRecipientId_throwsException() {
		ReflectionTestUtils.setField(notification, "id", null);
		notification.setSenderId(notification.getRecipientId());
		assertThrows(InvalidDataException.class, () -> notification.validateForCreation());
	}

	@Test
	public void validateForCreation_typeIsNull_throwsException() {
		ReflectionTestUtils.setField(notification, "id", null);
		notification.setType(null);
		assertThrows(InvalidDataException.class, () -> notification.validateForCreation());
	}

	@Test
	public void validateForCreation_referenceIdIsNull_throwsException() {
		ReflectionTestUtils.setField(notification, "id", null);
		notification.setReferenceId(null);
		assertThrows(InvalidDataException.class, () -> notification.validateForCreation());
	}

	@Test
	public void validateForCreation_noDataIssues_doesNotThrowException() {
		ReflectionTestUtils.setField(notification, "id", null);
		notification.validateForCreation();
	}

	@Test
	public void validateForUpdate_idIsNull_throwsException() {
		ReflectionTestUtils.setField(notification, "id", null);
		assertThrows(InvalidDataException.class, () -> notification.validateForUpdate());
	}

	@Test
	public void validateForUpdate_recipientIdIsNull_throwsException() {
		notification.setRecipientId(null);
		assertThrows(InvalidDataException.class, () -> notification.validateForUpdate());
	}

	@Test
	public void validateForUpdate_senderIdIsNull_throwsException() {
		notification.setSenderId(null);
		assertThrows(InvalidDataException.class, () -> notification.validateForUpdate());
	}

	@Test
	public void validateForUpdate_senderIdEqualsRecipientId_throwsException() {
		notification.setSenderId(notification.getRecipientId());
		assertThrows(InvalidDataException.class, () -> notification.validateForUpdate());
	}

	@Test
	public void validateForUpdate_typeIsNull_throwsException() {
		notification.setType(null);
		assertThrows(InvalidDataException.class, () -> notification.validateForUpdate());
	}

	@Test
	public void validateForUpdate_referenceIdIsNull_throwsException() {
		notification.setReferenceId(null);
		assertThrows(InvalidDataException.class, () -> notification.validateForUpdate());
	}

	@Test
	public void validateForUpdate_noDataIssues_doesNotThrowException() {
		notification.validateForUpdate();
	}
}
