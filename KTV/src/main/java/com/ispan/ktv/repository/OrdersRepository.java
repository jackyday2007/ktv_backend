package com.ispan.ktv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.dao.OrdersDAO;

public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersDAO {
	
	
//    @Query(value = "FROM Orders WHERE orderId = :orderId")
//    public Object findByOrdersId(Long orderId);
    
    

}
