package com.konnector.backendapi.contactdetail;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.jeasy.random.FieldPredicates.named;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/contactdetail/contact-detail-insert-data.sql" })
@Import(JpaContactDetailDao.class)
public class ContactDetailPersistenceIT {

	@Autowired
	private JpaContactDetailDao jpaContactDetailDao;

	private final EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters().excludeField(named("id")));
	private final ContactDetail contactDetail = easyRandom.nextObject(ContactDetail.class);

	@BeforeEach
	public void setup() {
		contactDetail.setUserId(1L);
	}

	@Test
	@Transactional
	public void saveContactDetail_savesContactDetail() {
		jpaContactDetailDao.save(contactDetail);
		assertEquals(contactDetail, jpaContactDetailDao.get(contactDetail.getId()).get());
	}

	@Test
	@Transactional
	public void updateContactDetail_updatesUser() {
		ContactDetail contactDetail = jpaContactDetailDao.get(1L).get();
		contactDetail.setValue("some new value");
		jpaContactDetailDao.update(contactDetail);
		jpaContactDetailDao.flush();
		ContactDetail updatedContactDetail = jpaContactDetailDao.get(contactDetail.getId()).get();
		assertEquals(contactDetail.getType(), updatedContactDetail.getType());
	}

	@Test
	@Transactional
	public void getContactDetail_getsContactDetail() {
		assertTrue(jpaContactDetailDao.get(1L).isPresent());
	}

	@Test
	@Transactional
	public void deleteContactDetail_deletesContactDetail() {
		ContactDetail contactDetail = jpaContactDetailDao.get(1L).get();
		jpaContactDetailDao.delete(contactDetail);
		assertTrue(jpaContactDetailDao.get(1L).isEmpty());
	}
}
