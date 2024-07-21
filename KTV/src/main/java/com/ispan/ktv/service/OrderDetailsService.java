package com.ispan.ktv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.repository.OrderDetailsRepository;
import com.ispan.ktv.repository.OrdersRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderDetailsService {
	
	
	@Autowired
	OrderDetailsRepository OrderDetailsRepo;
	
	@Autowired
	OrdersRepository OrdersRepo;
	
	
	public Double subTotal( Long orderId ) {
		if ( orderId != null ) {
			Optional<Orders> order = OrdersRepo.findById(orderId);
			if ( order.isPresent() ) {
				Optional<Double> orderDetails = OrderDetailsRepo.subTotal(order.get());
				if ( orderDetails.isPresent() ) {
					return orderDetails.get();
				}
			}
		}
		return null;
	}
	
	
	

}
