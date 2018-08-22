package com.qualys.repositories;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import com.qualys.entities.ScanDetails;

@Service
public class ScanDetailsRepositoryImpl implements ScanDetailsRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	public List<ScanDetails> listScan(Long fromScan, Long toScan, Integer size) throws RuntimeException {
		TypedQuery<ScanDetails> query = getScanDetailsTypedQuery(fromScan, toScan, size);
		List<ScanDetails> scanDetails = query.getResultList();
		return scanDetails;
	}

	private TypedQuery<ScanDetails> getScanDetailsTypedQuery(Long fromScan, Long toScan, Integer size) throws RuntimeException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ScanDetails> query = cb.createQuery(ScanDetails.class);
		Root<ScanDetails> root = query.from(ScanDetails.class);
		List<Predicate> predicates = new ArrayList<>();

		if(fromScan != null)
			predicates.add(cb.greaterThanOrEqualTo(root.get("submittedOn"), Date.from(Instant.ofEpochMilli(fromScan))));

		if(toScan != null)
			predicates.add(cb.lessThanOrEqualTo(root.get("submittedOn"), Date.from(Instant.ofEpochMilli(toScan))));

		CriteriaQuery<ScanDetails> criteriaQuery = query.select(root).where(predicates.toArray(new Predicate[]{}));

		criteriaQuery.orderBy(cb.desc(root.get("submittedOn")));
		TypedQuery<ScanDetails> scanDetailsTypedQuery = entityManager.createQuery(criteriaQuery);
		scanDetailsTypedQuery.setMaxResults((size != null) ? size : 10);
		return scanDetailsTypedQuery;
	}
}
