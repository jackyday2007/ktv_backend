package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.Customers;

public interface CustomersRepository extends JpaRepository<Customers, Integer> {

}
