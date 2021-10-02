package com.konnector.backendapi.contactdetail;

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
public class JpaContactDetailTest {

	@InjectMocks
	private final JpaContactDetailDao jpaContactDetailDao = new JpaContactDetailDao();

	@Mock
	private EntityManager entityManagerMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final ContactDetail contactDetail = easyRandom.nextObject(ContactDetail.class);

	@Test
	public void get_returnsContactDetail() {
		long contactDetailId = 1;
		when(entityManagerMock.find(ContactDetail.class, contactDetailId)).thenReturn(contactDetail);
		assertEquals(contactDetail, jpaContactDetailDao.get(contactDetailId).get());
	}

	@Test
	public void save_savesContactDetail() {
		jpaContactDetailDao.save(contactDetail);

		verify(entityManagerMock, times(1)).persist(contactDetail);
	}

	@Test
	public void update_updatesContactDetail() {
		jpaContactDetailDao.update(contactDetail);

		verify(entityManagerMock, times(1)).merge(contactDetail);
	}

	@Test
	public void delete_deletesDetail() {
		jpaContactDetailDao.delete(contactDetail);

		verify(entityManagerMock, times(1)).remove(contactDetail);
	}
}
