package com.ispan.ktv.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.service.OrderService;
import com.ispan.ktv.service.RoomService;
import com.ispan.ktv.util.DatetimeConverter;

@RestController
@RequestMapping("/ktvbackend/")
public class OrdersController {

	@Autowired
	OrderService orderService;

	@Autowired
	RoomService roomService;

	// @PostMapping("/orders/find")
	// public String ordersFind( @RequestBody JSONObject body ) {
	// return orderService.findOrders(body);
	// }

	@GetMapping("/orders/{ordersId}")
	public Map<String, Object> findByOrdersId(@PathVariable(name = "ordersId") Long ordersId) {
		// JSONObject responseBody = new JSONObject();
		// JSONArray array = new JSONArray();
		Orders orders = orderService.findByOrdersId(ordersId);
		if (orders != null) {
			Map<String, Object> order = new HashMap<>();
			String orderDate = DatetimeConverter.toString(orders.getOrderDate(), "yyyy-MM-dd");
			String startTime = DatetimeConverter.toString(orders.getStartTime(), "HH:mm:ss");
			String endTime = DatetimeConverter.toString(orders.getEndTime(), "HH:mm:ss");
			// JSONObject item = new JSONObject();
			order.put("orderId", orders.getOrderId());
			order.put("customerId", (orders.getCustomerId() != null ? orders.getCustomerId().getCustomerId() : ""));
			order.put("memberId", (orders.getMemberId() != null ? orders.getMemberId().getMemberId() : ""));
			order.put("room", orders.getRoom().getRoomId());
			order.put("numberOfPersons", orders.getNumberOfPersons());
			order.put("hours", orders.getHours());
			order.put("orderDate", orderDate);
			order.put("startTime", startTime);
			order.put("endTime", endTime);
			order.put("subTotal", orders.getSubTotal());
			// array.put(item);
			// System.out.println("array"+array);
			return order;
		}
		// responseBody.put("List", array);
		// return responseBody.toString();
		return Collections.emptyMap();
		// return orderService.findByOrdersId(ordersId);
	}

	@PostMapping("/orders/allOrders")
	public String findAll(@RequestBody(required = false) String body) {
		JSONObject responseBody = new JSONObject();
		List<Orders> result = orderService.findAll(body);
		System.out.println("result: " + result);
		JSONArray array = new JSONArray();
		if (result != null && !result.isEmpty()) {
			for (Orders orders : result) {
				String orderDate = DatetimeConverter.toString(orders.getOrderDate(), "yyyy-MM-dd");
				String startTime = DatetimeConverter.toString(orders.getStartTime(), "HH:mm:ss");
				String endTime = DatetimeConverter.toString(orders.getEndTime(), "HH:mm:ss");
				JSONObject item = new JSONObject();
				item.put("orderId", orders.getOrderId());
				item.put("customerId", (orders.getCustomerId() != null ? orders.getCustomerId().getCustomerId() : ""));
				item.put("memberId", (orders.getMemberId() != null ? orders.getMemberId().getMemberId() : ""));
				item.put("room", orders.getRoom().getRoomId());
				item.put("numberOfPersons", orders.getNumberOfPersons());
				item.put("hours", orders.getHours());
				item.put("orderDate", orderDate);
				item.put("startTime", startTime);
				item.put("endTime", endTime);
				item.put("subTotal", orders.getSubTotal());
				array.put(item);
			}
		}
		responseBody.put("list", array);
		return responseBody.toString();
	}

	@PostMapping("/orders/newOrder")
	public String newOrders(@RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		JSONObject obj = new JSONObject(body);
		Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
		Integer hours = obj.isNull("hours") ? null : obj.getInt("hours");
		String orderDate = obj.isNull("orderDate") ? null : obj.getString("orderDate");
		String startTime = obj.isNull("startTime") ? null : obj.getString("startTime");
		if (numberOfPersons == null) {
			responseBody.put("success", false);
			responseBody.put("message", "請填寫人數");
		} else {
			if (orderDate == null) {
				responseBody.put("success", false);
				responseBody.put("message", "請填寫預約日期");
			} else {
				if (hours == null) {
					responseBody.put("success", false);
					responseBody.put("message", "請填寫歡唱時數");
				} else {
					if (startTime == null) {
						responseBody.put("success", false);
						responseBody.put("message", "請填寫開始時間");
					} else {
						String newOrderId = orderService.generateOrderId();
						Long orderId = Long.valueOf(newOrderId);
						Orders order = new Orders();
						order.setOrderId(orderId);
						order.setNumberOfPersons(numberOfPersons);
						order.setHours(hours);
						order.setOrderDate(DatetimeConverter.parse(obj.getString("orderDate"), "yyyy-MM-dd"));
						order.setStartTime(DatetimeConverter.parse(obj.getString("startTime"), "HH:mm"));
						order.setEndTime(DatetimeConverter.parse(obj.getString("endTime"), "HH:mm"));
						order.setSubTotal(Double.valueOf("0.0"));
						Orders result = orderService.createOrders(order);
						if (result == null) {
							responseBody.put("success", false);
							responseBody.put("message", "預定失敗");
						} else {
							responseBody.put("success", true);
							responseBody.put("message", "預定成功");
						}
					}
				}
			}
		}

		return responseBody.toString();
	}

}
