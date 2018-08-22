package com.qualys.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qualys.entities.Domain;
import com.qualys.entities.ScanDetails;
import com.qualys.enums.ScanStatus;
import com.qualys.exceptions.ScanDetailNotFoundException;
import com.qualys.helpers.HtmlTagParser;
import com.qualys.repositories.DomainRepository;
import com.qualys.repositories.ScanDetailsRepository;


@Service
public class ScanServiceImpl implements ScanService {

	@Autowired
	private HtmlTagParser htmlTagParser;
	@Autowired
	private DomainRepository domainRepository;
	@Autowired
	private ScanDetailsRepository scanDetailsRepository;

	public ScanDetails submitUrlForAScan(String url) throws IOException {
			Domain domain = getDomainFromScanning(url);
			return getScanDetails(url, domain);
	}

	public List<ScanDetails> listScan(Long fromScan, Long toScan, Integer size) throws RuntimeException {
		return scanDetailsRepository.listScan(fromScan, toScan, size);
	}

	public ScanDetails getScanDetail(Long id) throws ScanDetailNotFoundException {
		Optional<ScanDetails> scanDetails = scanDetailsRepository.findById(id);
		if(scanDetails.orElse(null) == null)
			throw new ScanDetailNotFoundException("Scan Id not found");
		return scanDetails.get();
	}

	private ScanDetails getScanDetails(String url, Domain domain) throws IOException {
		ScanDetails details;
		try {
			details = htmlTagParser.startScanning(url);
			details.setDomainId(domain);
			scanDetailsRepository.save(details);
		} catch (IOException | IllegalArgumentException exception) {
			ScanDetails errorScan = new ScanDetails();
			errorScan.setDomainId(domain);
			errorScan.setWebsiteTitle("");
			errorScan.setStatus(ScanStatus.FAILED);
			errorScan.setSubmittedOn(new Date(System.currentTimeMillis()));
			scanDetailsRepository.save(errorScan);
			throw exception;
		}
		return details;
	}

	private Domain getDomainFromScanning(String url) throws IOException {
		Domain domain;
		try {
				domain = htmlTagParser.scanForDomain(url);
				domainRepository.save(domain);
		} catch (IOException | IllegalArgumentException argumentException) {
			Domain errorDomain = new Domain();
			errorDomain.setIpAddress("");
			errorDomain.setHostname(url);
			domainRepository.save(errorDomain);
			ScanDetails errorScan = new ScanDetails();
			errorScan.setDomainId(errorDomain);
			errorScan.setWebsiteTitle("");
			errorScan.setStatus(ScanStatus.FAILED);
			errorScan.setSubmittedOn(new Date(System.currentTimeMillis()));
			scanDetailsRepository.save(errorScan);
			throw argumentException;
		}
		return domain;
	}
}
