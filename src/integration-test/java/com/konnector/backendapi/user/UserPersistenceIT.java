package com.konnector.backendapi.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-integration-test.properties")
@Import(JpaUserDao.class)
public class UserPersistenceIT {

	@Autowired
	private JpaUserDao userDao;

	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final User user = podamFactory.manufacturePojo(User.class);

	@Test
	@Transactional
	public void saveUser_savesUser() {
		userDao.save(user);
		assertEquals(user, userDao.get(user.getId()).get());
	}
}
