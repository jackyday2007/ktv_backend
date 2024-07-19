package com.ispan.ktv.service;

import java.util.Optional;

import org.json.JSONObject;
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
	
	// 取消預約
	public OrdersStatusHistory noCheckIn(String body) {
		JSONObject obj = new JSONObject(body);
		Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
		Optional<Orders> optional = ordersRepository.findById(orderId);
		if ( optional.isPresent() ) {
			Orders update = optional.get();
			OrdersStatusHistory history = new OrdersStatusHistory();
			if ( update != null ) {
				history.setOrderId(update);
				history.setStatus("取消預約");
				OrdersStatusHistory result = OrdersStatusHistoryRepo.save(history);
				return result;
			}
		}
		return null;
	}
	
	


}
