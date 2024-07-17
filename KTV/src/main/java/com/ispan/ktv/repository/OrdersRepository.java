package com.ispan.ktv.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ispan.ktv.bean.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {

   long countByOrderDate(Date orderDate);

   boolean existsByOrderId(Long orderId);

}
