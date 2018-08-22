package com.qualys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qualys.entities.Domain;


@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {
}
