package com.konnector.backendapi.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaUserDaoTest {

	@InjectMocks
	private final JpaUserDao jpaUserDao = new JpaUserDao();

	@Mock
	private EntityManager entityManagerMock;

	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final User user = podamFactory.manufacturePojo(User.class);

	@Test
	public void get_returnsUser() {
		when(entityManagerMock.find(User.class, user.getId())).thenReturn(user);
		assertEquals(user, jpaUserDao.get(user.getId()).get());
	}

	@Test
	public void save_savesUser() {
		jpaUserDao.save(user);

		verify(entityManagerMock, times(1)).persist(user);
	}

	@Test
	public void update_updatesUser() {
		jpaUserDao.update(user);

		verify(entityManagerMock, times(1)).merge(user);
	}

	@Test
	public void delete_deletesUser() {
		jpaUserDao.delete(user);

		verify(entityManagerMock, times(1)).remove(user);
	}
}
