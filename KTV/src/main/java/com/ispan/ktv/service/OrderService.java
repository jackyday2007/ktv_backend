package com.ispan.ktv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.repository.OrdersRepository;

@Service
public class OrderService {
	
	@Autowired
	OrdersRepository ordersRepository;
	
	
	public Orders findByOrdersId( Long ordersId ) {
		if ( ordersId != null ) {
			Optional<Orders> optional = ordersRepository.findById(ordersId);
			if ( optional.isPresent() ) {
				return optional.get();
			}
		}
		return null;
	}
	

}
