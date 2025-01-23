package com.retailer.rewards.repository;

import com.retailer.rewards.dto.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<List<Customer>> findByCustomerId(Long id);
};