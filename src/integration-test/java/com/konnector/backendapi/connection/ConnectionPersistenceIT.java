package com.konnector.backendapi.connection;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.jeasy.random.FieldPredicates.named;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/connection/connection-insert-data.sql" })
@Import(JpaConnectionDao.class)
public class ConnectionPersistenceIT {

	@Autowired
	private JpaConnectionDao jpaConnectionDao;

	private final EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters().excludeField(named("id")));
	private final Connection connection = easyRandom.nextObject(Connection.class);

	@Test
	@Transactional
	public void save_savesConection() {
		connection.setRequesteeId(2L);
		connection.setRequesterId(3L);
		jpaConnectionDao.save(connection);
		assertEquals(connection, jpaConnectionDao.get(connection.getId()).get());
	}

	@Test
	@Transactional
	public void update_updatesConnection() {
		Connection connection = jpaConnectionDao.get(1L).get();
		connection.setStatus(ConnectionStatus.ACCEPTED);
		jpaConnectionDao.update(connection);
		jpaConnectionDao.flush();
		Connection updatedConnection = jpaConnectionDao.get(connection.getId()).get();
		assertEquals(connection.getRequesterId(), updatedConnection.getRequesterId());
	}

	@Test
	@Transactional
	public void get_getsConnection() {
		assertTrue(jpaConnectionDao.get(1L).isPresent());
	}

	@Test
	@Transactional
	public void delete_deletesConnection() {
		Connection connection = jpaConnectionDao.get(1L).get();
		jpaConnectionDao.delete(connection);
		assertTrue(jpaConnectionDao.get(1L).isEmpty());
	}
}
