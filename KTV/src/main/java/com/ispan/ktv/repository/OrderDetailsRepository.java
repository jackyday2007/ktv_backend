package com.ispan.ktv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.OrderDetails;
import com.ispan.ktv.bean.Orders;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
	
	
//	select SUM(subTotal) from orderDetails where orderId = 20240719001
	@Query(value = " SELECT SUM(od.subTotal) FROM OrderDetails AS od WHERE orderId = :orderId ")
	Optional<Double> subTotal(@Param(value = "orderId") Orders orders);
	

}
