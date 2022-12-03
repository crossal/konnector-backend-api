package com.konnector.backendapi.verification;

import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.jeasy.random.FieldPredicates.named;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-integration-test.properties")
public class VerificationPersistenceIT {

	@Autowired
	private VerificationRepository verificationRepository;
	@Autowired
	private UserRepository userRepository;

	private final EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters().excludeField(named("id")));
	private final User user = easyRandom.nextObject(User.class);
	private final Verification verification = easyRandom.nextObject(Verification.class);

	@Test
	@Transactional
	public void saveVerification_savesVerification() {
		userRepository.save(user);
		verification.setUserId(user.getId());
		verification.setCode(verification.getCode().substring(0, 6));
		verification.setCodeAttemptsLeft(5);

		verificationRepository.save(verification);

		assertEquals(verification, verificationRepository.findById(verification.getId()).get());
	}
}
