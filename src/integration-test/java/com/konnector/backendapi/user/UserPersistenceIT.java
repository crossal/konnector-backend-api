package com.konnector.backendapi.user;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.jeasy.random.FieldPredicates.named;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/user/user-insert-data.sql" })
public class UserPersistenceIT {

	@Autowired
	private UserRepository userRepository;

	private final EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters().excludeField(named("id")));
	private final User user = easyRandom.nextObject(User.class);

	@Test
	@Transactional
	public void saveUser_savesUser() {
		String sixtyCharacterHashedPassword = "123456789012345678901234567890123456789012345678901234567890";
		user.setPassword(sixtyCharacterHashedPassword);
		userRepository.save(user);
		assertEquals(user, userRepository.findById(user.getId()).get());
		assertEquals(sixtyCharacterHashedPassword, user.getPassword());
	}

	@Test
	@Transactional
	public void updateUser_updatesUser() {
		User user = userRepository.findById(1L).get();
		user.setLastName("some new last name");
		userRepository.save(user);
		User updatedUser = userRepository.findById(user.getId()).get();
		assertEquals(user.getUsername(), updatedUser.getUsername());
	}

	@Test
	@Transactional
	public void getUser_getsUser() {
		assertTrue(userRepository.findById(1L).isPresent());
	}
}
