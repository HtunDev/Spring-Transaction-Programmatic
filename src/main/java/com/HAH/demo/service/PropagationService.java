package com.HAH.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.HAH.demo.repo.DetailsRepository;
import com.HAH.demo.repo.HeaderRepository;

@Service
public class PropagationService {

	@Autowired
	private DetailsRepository detailsRepository;

	@Autowired
	private HeaderRepository headerRepository;

	@Autowired
	private PlatformTransactionManager txManager;

	public Result save(int state, String header, String... details) {

		var txAttributes = new DefaultTransactionDefinition();
		txAttributes.setPropagationBehavior(TransactionAttribute.PROPAGATION_REQUIRED);
		txAttributes.setIsolationLevel(TransactionAttribute.ISOLATION_SERIALIZABLE);
		txAttributes.setTimeout(5);

		// Start Transaction
		TransactionStatus status = txManager.getTransaction(txAttributes);

		try {
			// create header
			var headerId = headerRepository.create(header);
			if (state == 1) {
				throw new RuntimeException();
			}
			// create details
			var detailsId = detailsRepository.create(headerId, details);
			if (state == 2) {
				throw new RuntimeException();
			}
			// Commit
			txManager.commit(status);
			return new Result(headerId, detailsId);
		} catch (Exception e) {
			// Rollback
			txManager.rollback(status);

			throw new RuntimeException(e);
		}
	}
}
