
package com.qualys.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qualys.entities.Domain;
import com.qualys.entities.ScanDetails;
import com.qualys.enums.ScanStatus;



@RunWith(SpringRunner.class)
@SpringBootTest
public class HtmlTagParserTest {

	private String url;
	private HtmlTagParser htmlTagParser;

	@Before
	public void init() {
		url = "https://google.co.in";
		htmlTagParser = new HtmlTagParser();
	}
	@Test
	public void test_scan_for_domain_success() throws Exception {
		Domain actualDomain = htmlTagParser.scanForDomain(url);
		assertEquals(actualDomain.getHostname(), "google.co.in");
		assertNotNull(actualDomain.getIpAddress());
	}

	@Test(expected = MalformedURLException.class)
	public void test_scan_for_domain_failure() throws Exception {
		Domain actualDomain = htmlTagParser.scanForDomain("asdfasdf");
	}

	@Test
	public void test_start_scanning_success() throws Exception {
		ScanDetails scanDetails = htmlTagParser.startScanning(url);
		assertNotNull(scanDetails);
		assertEquals(scanDetails.getStatus(), ScanStatus.SUCCESS);
	}
}

