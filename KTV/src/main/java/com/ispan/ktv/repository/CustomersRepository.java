package com.ispan.ktv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.Customers;

public interface CustomersRepository extends JpaRepository<Customers, Integer> {
	
	@Query(value = "FROM Customers WHERE idNumber = :idNumber")
	Optional<Customers> customerId(@Param(value="idNumber") String idNumber);

}
