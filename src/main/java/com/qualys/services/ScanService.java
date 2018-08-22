package com.qualys.services;

import java.io.IOException;
import java.util.List;

import com.qualys.entities.ScanDetails;
import com.qualys.exceptions.ScanDetailNotFoundException;


public interface ScanService {
	ScanDetails submitUrlForAScan(String url) throws IOException;
	List<ScanDetails> listScan(Long fromScan, Long toScan, Integer size) throws Exception;
	ScanDetails getScanDetail(Long id) throws ScanDetailNotFoundException;
}
