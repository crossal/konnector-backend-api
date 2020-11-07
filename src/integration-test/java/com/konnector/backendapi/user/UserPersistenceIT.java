package com.konnector.backendapi.user;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaUserDao.class)
public class UserPersistenceIT {

	@Autowired
	private JpaUserDao userDao;

	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final User user = podamFactory.manufacturePojo(User.class);

//	private static final Random random = new Random();

//	@Test
//	@Transactional
//	public void canSaveAndGetUser() {
//		User user = new User();
//		user.setFirstName("John");
//		user.setLastName("Doe");
//		user.setUsername("johndoe123");
//		user.setEmail("johndoe@gmail.com");
//		byte[] password = new byte[16];
//		random.nextBytes(password);
//		user.setPassword(password);
//		byte[] salt = new byte[16];
//		random.nextBytes(salt);
//		user.setSalt(salt);
//
//		userDao.save(user);
//
//		User fetchedUser = userDao.get(1).get();
//
//		assertEquals(user, fetchedUser);
//	}

	@Test
	@Transactional
	public void saveUser_savesUser() {
		user.setId(null);
		userDao.save(user);
		assertEquals(user, userDao.get(user.getId()).get());
	}

	@Test
	public void saveUser_missingEmail_notAllowed() {

	}
}
