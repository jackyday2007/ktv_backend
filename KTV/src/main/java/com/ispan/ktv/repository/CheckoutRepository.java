package com.ispan.ktv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.Checkout;

public interface CheckoutRepository extends JpaRepository<Checkout, Integer> {

	@Query(value = "From Checkout where orderId = :orderId")
	Optional<Checkout> findCheckout(@Param(value="orderId") Long orderId );
	
	
	
}
