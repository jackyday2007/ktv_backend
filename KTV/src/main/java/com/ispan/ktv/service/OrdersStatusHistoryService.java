package com.ispan.ktv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.bean.OrdersStatusHistory;
import com.ispan.ktv.repository.OrdersRepository;
import com.ispan.ktv.repository.OrdersStatusHistoryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdersStatusHistoryService {
	
	@Autowired
	OrdersStatusHistoryRepository OrdersStatusHistoryRepo;
	
	@Autowired
	OrdersRepository ordersRepository;
	
	
	
	public OrdersStatusHistory findNewHistory( Long ordersId ) {
		System.out.println("ordersId="+ordersId);
		Optional<Orders> check = ordersRepository.findById(ordersId);
		System.out.println("check=" + check);
		if ( check != null ) {
			Optional<OrdersStatusHistory> optional = OrdersStatusHistoryRepo.history(check.get());
			System.out.println("optional=" + optional);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	
	
	
	


}
