package com.konnector.backendapi.connection;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaConnectionDaoTest {

	@InjectMocks
	private final JpaConnectionDao jpaConnectionDao = new JpaConnectionDao();

	@Mock
	private EntityManager entityManagerMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final Connection connection = easyRandom.nextObject(Connection.class);

	@Test
	public void get_returnsConnection() {
		long connectionId = 1;
		when(entityManagerMock.find(Connection.class, connectionId)).thenReturn(connection);
		assertEquals(connection, jpaConnectionDao.get(connectionId).get());
	}

	@Test
	public void save_savesConnection() {
		jpaConnectionDao.save(connection);

		verify(entityManagerMock).persist(connection);
	}

	@Test
	public void update_updatesConnection() {
		jpaConnectionDao.update(connection);

		verify(entityManagerMock).merge(connection);
	}

	@Test
	public void delete_deletesConnection() {
		jpaConnectionDao.delete(connection);

		verify(entityManagerMock).remove(connection);
	}
}
