package com.ispan.ktv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.bean.OrdersStatusHistory;

public interface OrdersStatusHistoryRepository extends JpaRepository<OrdersStatusHistory, Integer> {
	
	@Query(value = " SELECT osh FROM OrdersStatusHistory AS osh"
			+ " WHERE osh.orderId = :orderId "
			+ " AND osh.createTime = ( SELECT MAX(osh2.createTime) FROM OrdersStatusHistory osh2 "
			+ " WHERE osh2.orderId = :orderId ) ")
	 Optional<OrdersStatusHistory> history(@Param(value = "orderId") Orders orderId);
	
	@Query( value = "SELECT status FROM OrdersStatusHistory WHERE orderId = :orderId " )
	Optional<OrdersStatusHistory> orderId(@Param(value = "orderId") Long orderId );
	
	
	
}
