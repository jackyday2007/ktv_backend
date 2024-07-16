package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.OrdersStatusHistory;

public interface OrdersStatusHistoryRepository extends JpaRepository<OrdersStatusHistory, Integer> {

}
