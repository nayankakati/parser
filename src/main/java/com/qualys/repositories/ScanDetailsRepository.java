package com.qualys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qualys.entities.ScanDetails;


@Repository
public interface ScanDetailsRepository extends JpaRepository<ScanDetails, Long>, ScanDetailsRepositoryCustom {
}
