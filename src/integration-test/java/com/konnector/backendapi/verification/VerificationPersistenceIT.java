package com.konnector.backendapi.verification;

import com.konnector.backendapi.user.JpaUserDao;
import com.konnector.backendapi.user.User;
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
@TestPropertySource("classpath:application-test.properties")
@Import({JpaVerificationDao.class, JpaUserDao.class})
public class VerificationPersistenceIT {

	@Autowired
	private JpaVerificationDao verificationDao;
	@Autowired
	private JpaUserDao userDao;

	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final User user = podamFactory.manufacturePojo(User.class);
	private final Verification verification = podamFactory.manufacturePojo(Verification.class);

	@Test
	@Transactional
	public void saveVerification_savesVerification() {
		userDao.save(user);
		verification.setUserId(user.getId());
		verification.setCode(verification.getCode().substring(0, 6));
		verification.setCodeAttemptsLeft(5);

		verificationDao.save(verification);

		assertEquals(verification, verificationDao.get(verification.getId()).get());
	}
}
