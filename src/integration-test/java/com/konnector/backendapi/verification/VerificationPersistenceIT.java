package com.konnector.backendapi.verification;

import com.konnector.backendapi.verification.JpaVerificationDao;
import com.konnector.backendapi.verification.Verification;
import org.junit.jupiter.api.BeforeEach;
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
@Import(JpaVerificationDao.class)
public class VerificationPersistenceIT {

	@Autowired
	private JpaVerificationDao verificationDao;

	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final Verification verification = podamFactory.manufacturePojo(Verification.class);

	@BeforeEach
	public void setup() {
		verification.setUserId(1L);
		verification.setCode(verification.getCode().substring(0, 6));
		verification.setCodeAttemptsLeft(5);
	}

	@Test
	@Transactional
	public void saveVerification_savesVerification() {
		verificationDao.save(verification);
		assertEquals(verification, verificationDao.get(verification.getId()).get());
	}
}
