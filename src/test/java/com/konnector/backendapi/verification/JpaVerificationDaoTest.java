package com.konnector.backendapi.verification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

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

	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final Verification verification = podamFactory.manufacturePojo(Verification.class);

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
