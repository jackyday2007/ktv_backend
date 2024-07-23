package com.ispan.ktv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.OrderMenus;
import com.ispan.ktv.bean.Staff;

public interface OrderMenusRepository extends JpaRepository<OrderMenus, Integer>, JpaSpecificationExecutor<OrderMenus> {
	
	
	@Query(value = "SELECT category FROM OrderMenus GROUP BY category")
	OrderMenus categoryList(String categoryList );
	
	
	
    @Query(value="from OrderMenus where itemName like :name")
    List<Staff> findByName(@Param("name") String name);
}
