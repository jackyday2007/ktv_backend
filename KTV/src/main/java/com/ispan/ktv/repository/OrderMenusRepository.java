package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ispan.ktv.bean.OrderMenus;

public interface OrderMenusRepository extends JpaRepository<OrderMenus, Integer>, JpaSpecificationExecutor<OrderMenus> {

}
