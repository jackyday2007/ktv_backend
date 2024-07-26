package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.Checkout;

public interface CheckoutRepository extends JpaRepository<Checkout, Integer> {

}
