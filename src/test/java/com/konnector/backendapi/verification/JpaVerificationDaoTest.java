package com.konnector.backendapi.verification;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaVerificationDaoTest {

	@InjectMocks
	private JpaVerificationDao jpaVerificationDao = new JpaVerificationDao();

	@Mock
	private EntityManager entityManagerMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final Verification verification = easyRandom.nextObject(Verification.class);

	@Test
	public void get_returnsVerification() {
		long verificationId = 1;
		when(entityManagerMock.find(Verification.class, verificationId)).thenReturn(verification);
		assertEquals(verification, jpaVerificationDao.get(verificationId).get());
	}

	@Test
	public void save_savesVerification() {
		jpaVerificationDao.save(verification);

		verify(entityManagerMock, times(1)).persist(verification);
	}

	@Test
	public void update_updatesVerification() {
		jpaVerificationDao.update(verification);

		verify(entityManagerMock, times(1)).merge(verification);
	}

	@Test
	public void delete_deletesVerification() {
		jpaVerificationDao.delete(verification);

		verify(entityManagerMock, times(1)).remove(verification);
	}
}
