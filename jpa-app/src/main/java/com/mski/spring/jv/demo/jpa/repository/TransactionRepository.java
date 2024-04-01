package com.mski.spring.jv.demo.jpa.repository;

import com.mski.spring.jv.demo.jpa.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
