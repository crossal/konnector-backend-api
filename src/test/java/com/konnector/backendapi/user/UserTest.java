package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

	private final EasyRandom easyRandom = new EasyRandom();
	private final User user = easyRandom.nextObject(User.class);

	@BeforeEach
	public void setup() {
		user.setEmailVerified(false);
	}

	@Test
	public void validateForCreation_idIsNotNull_throwsException() {
		ReflectionTestUtils.setField(user, "id", 1L);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_emailIsNull_throwsException() {
		user.setEmail(null);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_emailIsEmpty_throwsException() {
		user.setEmail("");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_emailIsInvalid_throwsException() {
		user.setEmail("invalid_email");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_usernameIsNull_throwsException() {
		user.setUsername(null);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_usernameIsEmpty_throwsException() {
		user.setUsername("");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_firstNameIsNull_throwsException() {
		user.setFirstName(null);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_firstNameIsEmpty_throwsException() {
		user.setFirstName("");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_lastNameIsNull_throwsException() {
		user.setLastName(null);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_lastNameIsEmpty_throwsException() {
		user.setLastName("");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_passwordIsNull_throwsException() {
		user.setPassword(null);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_passwordIsEmpty_throwsException() {
		user.setPassword("");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_passwordIsTooShort_throwsException() {
		user.setPassword("123456");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_passwordIsTooLong_throwsException() {
		user.setPassword("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForCreation_emailVerificationIsTrue_throwsException() {
		user.setEmailVerified(true);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateForUpdate_idIsNull_throwsException() {
		ReflectionTestUtils.setField(user, "id", null);
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_passwordIsTooShort_throwsException() {
		user.setPassword("123456");
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_passwordIsTooLong_throwsException() {
		user.setPassword("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_emailIsNull_throwsException() {
		user.setPassword(null);
		user.setEmail(null);
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_emailIsEmpty_throwsException() {
		user.setPassword(null);
		user.setEmail("");
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_emailIsInvalid_throwsException() {
		user.setPassword(null);
		user.setEmail("invalid_email");
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_usernameIsNull_throwsException() {
		user.setPassword(null);
		user.setUsername(null);
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_usernameIsEmpty_throwsException() {
		user.setPassword(null);
		user.setUsername("");
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_firstNameIsNull_throwsException() {
		user.setPassword(null);
		user.setFirstName(null);
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_firstNameIsEmpty_throwsException() {
		user.setPassword(null);
		user.setFirstName("");
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_lastNameIsNull_throwsException() {
		user.setPassword(null);
		user.setLastName(null);
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void validateForUpdate_lastNameIsEmpty_throwsException() {
		user.setPassword(null);
		user.setLastName("");
		assertThrows(InvalidDataException.class, () -> user.validateForUpdate());
	}

	@Test
	public void merge_mergesUsers() {
		User user1 = new User();
		user1.setEmail("test1@email.com");
		user1.setUsername("username1");
		user1.setPassword("password1");
		user1.setFirstName("first_name1");
		user1.setLastName("last_name1");
		user1.setEmailVerified(false);
		ReflectionTestUtils.setField(user1, "id", 1L);

		User user2 = new User();
		user2.setEmail("test2@email.com");
		user2.setUsername("username2");
		user2.setPassword("password2");
		user2.setFirstName("first_name2");
		user2.setLastName("last_name2");
		user2.setEmailVerified(true);
		ReflectionTestUtils.setField(user1, "id", 2L);

		user1.merge(user2);

		assertNotEquals(user1.getId(), user2.getId());
		assertNotEquals(user1.getUsername(), user2.getUsername());
		assertNotEquals(user1.getEmail(), user2.getEmail());
		assertNotEquals(user1.isEmailVerified(), user2.isEmailVerified());
		assertEquals(user1.getFirstName(), user2.getFirstName());
		assertEquals(user1.getLastName(), user2.getLastName());
	}
}
