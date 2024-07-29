package com.ispan.ktv.service;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Checkout;
import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.bean.OrdersStatusHistory;
import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.repository.CheckoutRepository;
import com.ispan.ktv.repository.OrderDetailsRepository;
import com.ispan.ktv.repository.OrdersRepository;
import com.ispan.ktv.repository.OrdersStatusHistoryRepository;
import com.ispan.ktv.repository.RoomsRepository;

import jakarta.transaction.Transactional;

@Service
public class CheckoutService {
	
	@Autowired
	CheckoutRepository checkoutRepo;
	
	@Autowired
	OrdersRepository ordersRepo;
	
	@Autowired
	OrderDetailsRepository orderDetailsRepo;
	
	@Autowired
	OrdersStatusHistoryRepository ordersStatusHistoryRepo;
	
	@Autowired
	RoomsRepository roomsRepo;
	
	
	@Transactional
	public Checkout insertCheckout( String body ) {
		JSONObject obj = new JSONObject(body);
		Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
		System.out.println("orderId = " + orderId);
		String pay = obj.isNull("pay") ? null : obj.getString("pay");
		Integer roomId = obj.isNull("room") ? null : obj.getInt("room");
		System.out.println("roomId = " + roomId);
		Optional<Orders> findOrderId = ordersRepo.findById(orderId);
		if ( findOrderId.isPresent() ) {
			Optional<Double> total = orderDetailsRepo.subTotal(findOrderId.get());
			if ( total != null ) {
				Checkout checkout = new Checkout();
				checkout.setOrderId(orderId);
				checkout.setPay(Double.valueOf(pay));
				checkout.setChange(Double.valueOf(pay) - total.get());
				Checkout result = checkoutRepo.save(checkout);
				if ( result != null ) {
					OrdersStatusHistory osh = new OrdersStatusHistory();
					Optional<Rooms> room = roomsRepo.findById(roomId);
					Rooms rooms = room.get();
					osh.setOrderId(findOrderId.get());
					osh.setStatus("已結帳");
					rooms.setStatus("開放中");
					ordersStatusHistoryRepo.save(osh);
					roomsRepo.save(rooms);
					return result;
				}
			}
		}
		return null;
	}
	
	public Checkout findCheckout( Long orderId ) {
		Optional<Orders> orders = ordersRepo.findById(orderId);
		if ( orders.isPresent() ) {
			Optional<Checkout> result = checkoutRepo.findCheckout(orderId);
			System.out.println("result = " + result);
			if ( result.isPresent() ) {
				return result.get();
			}
		}
		return null;
	}
	
	

}
