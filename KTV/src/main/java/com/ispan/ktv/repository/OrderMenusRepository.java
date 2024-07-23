package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ispan.ktv.bean.OrderMenus;

public interface OrderMenusRepository extends JpaRepository<OrderMenus, Integer>, JpaSpecificationExecutor<OrderMenus> {
	
	
	@Query(value = "SELECT category FROM OrderMenus GROUP BY category")
	OrderMenus categoryList(String categoryList );
	
	
	
}
