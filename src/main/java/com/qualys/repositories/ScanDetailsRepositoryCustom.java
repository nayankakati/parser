package com.qualys.repositories;

import java.util.List;

import com.qualys.entities.ScanDetails;


public interface ScanDetailsRepositoryCustom {
	List<ScanDetails> listScan(Long fromScan, Long toScan, Integer size) throws RuntimeException;
}
