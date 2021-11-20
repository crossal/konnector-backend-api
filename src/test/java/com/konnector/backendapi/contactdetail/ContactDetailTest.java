package com.konnector.backendapi.contactdetail;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContactDetailTest {

	private final EasyRandom easyRandom = new EasyRandom();
	private final ContactDetail contactDetail = easyRandom.nextObject(ContactDetail.class);

	@Test
	public void validateForCreation_idIsNotNull_throwsException() {
		ReflectionTestUtils.setField(contactDetail, "id", 1L);
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForCreation());
	}

	@Test
	public void validateForCreation_userIdIsNull_throwsException() {
		contactDetail.setUserId(null);
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForCreation());
	}

	@Test
	public void validateForCreation_typeIsNull_throwsException() {
		contactDetail.setType(null);
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForCreation());
	}

	@Test
	public void validateForCreation_typeIsEmpty_throwsException() {
		contactDetail.setType("");
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForCreation());
	}

	@Test
	public void validateForCreation_valueIsNull_throwsException() {
		contactDetail.setValue(null);
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForCreation());
	}

	@Test
	public void validateForCreation_valueIsEmpty_throwsException() {
		contactDetail.setValue("");
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForCreation());
	}

	@Test
	public void validateForCreation_noDataIssues_doesNotThrowException() {
		ReflectionTestUtils.setField(contactDetail, "id", null);
		contactDetail.validateForCreation();
	}

	@Test
	public void validateForUpdate_idIsNull_throwsException() {
		ReflectionTestUtils.setField(contactDetail, "id", null);
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForUpdate());
	}

	@Test
	public void validateForUpdate_userIdIsNull_throwsException() {
		contactDetail.setUserId(null);
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForUpdate());
	}

	@Test
	public void validateForUpdate_typeIsNull_throwsException() {
		contactDetail.setType(null);
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForUpdate());
	}

	@Test
	public void validateForUpdate_typeIsEmpty_throwsException() {
		contactDetail.setType("");
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForUpdate());
	}

	@Test
	public void validateForUpdate_valueIsNull_throwsException() {
		contactDetail.setValue(null);
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForUpdate());
	}

	@Test
	public void validateForUpdate_valueIsEmpty_throwsException() {
		contactDetail.setValue("");
		assertThrows(InvalidDataException.class, () -> contactDetail.validateForUpdate());
	}

	@Test
	public void validateForUpdate_noDataIssues_doesNotThrowException() {
		contactDetail.validateForUpdate();
	}

	@Test
	public void merge_merges() {
		ContactDetail contactDetail1 = new ContactDetail();
		contactDetail1.setUserId(1L);
		contactDetail1.setType("type1");
		contactDetail1.setValue("value1");
		ReflectionTestUtils.setField(contactDetail1, "id", 1L);

		ContactDetail contactDetail2 = new ContactDetail();
		contactDetail2.setUserId(2L);
		contactDetail2.setType("type2");
		contactDetail2.setValue("value2");
		ReflectionTestUtils.setField(contactDetail1, "id", 2L);

		contactDetail1.merge(contactDetail2);

		assertNotEquals(contactDetail1.getId(), contactDetail2.getId());
		assertEquals(contactDetail1.getType(), contactDetail2.getType());
		assertEquals(contactDetail1.getValue(), contactDetail2.getValue());
	}
}
