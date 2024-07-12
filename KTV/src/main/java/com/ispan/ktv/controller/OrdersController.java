package com.ispan.ktv.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.service.OrderService;
import com.ispan.ktv.util.DatetimeConverter;

@RestController
@RequestMapping("/ktvbackend/")
public class OrdersController {
	
	@Autowired
	OrderService orderService;
	
//	@PostMapping("/orders/find")
//	public String ordersFind( @RequestBody JSONObject body ) {
//		return orderService.findOrders(body);
//	}
	
	@GetMapping("/orders/{ordersId}")
	public Map<String, Object> findByOrdersId( @PathVariable(name= "ordersId") Long ordersId ) {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		Orders orders = orderService.findByOrdersId(ordersId);
		if ( orders != null ) {
            Map<String, Object> order = new HashMap<>();
			String orderDate = DatetimeConverter.toString(orders.getOrderDate(), "yyyy-MM-dd");
			String startTime = DatetimeConverter.toString(orders.getStartTime(), "HH:mm:ss");
			String endTime = DatetimeConverter.toString(orders.getEndTime(), "HH:mm:ss");
//			JSONObject item = new JSONObject();
			order.put("orderId", orders.getOrderId());
			order.put("customerId", (orders.getCustomerId() != null ? orders.getCustomerId().getCustomerId() : null));
			order.put("memberId", (orders.getMemberId() !=null ? orders.getMemberId().getMemberId() : null));
			order.put("room", orders.getRoom().getRoomId());
			order.put("numberOfPersons", orders.getNumberOfPersons());
			order.put("hours", orders.getHours());
			order.put("orderDate", orderDate);
			order.put("startTime", startTime);
			order.put("endTime", endTime);
			order.put("subTotal", orders.getSubTotal());
//			array.put(item);
//			System.out.println("array"+array);
			return order;
		}
//		responseBody.put("List", array);
		System.out.println("responseBody"+responseBody);
//		return responseBody.toString();
		return Collections.emptyMap();
//		return orderService.findByOrdersId(ordersId);
	}
	
	
	

}

