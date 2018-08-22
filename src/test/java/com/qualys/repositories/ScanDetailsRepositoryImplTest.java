package com.qualys.repositories;

import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.qualys.entities.Domain;
import com.qualys.entities.ScanDetails;
import com.qualys.enums.ScanStatus;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ScanDetailsRepositoryImplTest {

	@Autowired
	ScanDetailsRepositoryImpl scanDetailsRepositoryImpl;

	@Autowired
	private TestEntityManager testEntityManager;

	private Domain domain;
	private ScanDetails scanDetails;

	@Test
	public void test_scan_list_success() {
		domain = new Domain();
		domain.setHostname("google.in");
		domain.setIpAddress("10.10.10.10");
		domain.setRedirectionUrl("google.com");
		testEntityManager.persist(domain);

		scanDetails = new ScanDetails();
		scanDetails.setDomainId(domain);
		scanDetails.setWebsiteBody("website body");
		scanDetails.setWebsiteTitle("website title");
		scanDetails.setSubmittedOn(Date.from(Instant.ofEpochMilli(1529336856297l)));
		scanDetails.setStatus(ScanStatus.SUCCESS);
		scanDetails.setImgCount(10l);
		scanDetails.setLinksCount(25l);
		testEntityManager.persist(scanDetails);
		List<ScanDetails> ll = scanDetailsRepositoryImpl.listScan(1529336856297l,1529336856298l,5);
		assertNotNull(ll);
		testEntityManager.remove(domain);
		testEntityManager.remove(scanDetails);
	}
}
