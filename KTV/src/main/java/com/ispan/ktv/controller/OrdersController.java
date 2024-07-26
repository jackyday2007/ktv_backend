package com.ispan.ktv.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.bean.OrdersStatusHistory;
import com.ispan.ktv.service.CustomerService;
import com.ispan.ktv.service.MemberService;
import com.ispan.ktv.service.OrderDetailsService;
import com.ispan.ktv.service.OrderService;
import com.ispan.ktv.service.OrdersStatusHistoryService;
import com.ispan.ktv.service.RoomService;
import com.ispan.ktv.util.DatetimeConverter;

@RestController
@RequestMapping("/ktvbackend/")
@CrossOrigin
public class OrdersController {

	@Autowired
	OrderService orderService;

	@Autowired
	RoomService roomService;

	@Autowired
	MemberService memberService;

	@Autowired
	CustomerService customerService;

	@Autowired
	OrdersStatusHistoryService oshService;

	@Autowired
	OrderDetailsService orderDetailsService;

	@GetMapping("/orders/{ordersId}")
	public String findByOrdersId(@PathVariable(name = "ordersId") Long ordersId) {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		Orders orders = orderService.findByOrdersId(ordersId);
		if (orders != null) {
			String orderDate = DatetimeConverter.toString(orders.getOrderDate(), "yyyy-MM-dd");
			String startTime = DatetimeConverter.toString(orders.getStartTime(), "HH:mm");
			String endTime = DatetimeConverter.toString(orders.getEndTime(), "HH:mm");
			OrdersStatusHistory status = oshService.findNewHistory(orders.getOrderId());
			Double subTotal = orderDetailsService.subTotal(ordersId);
			JSONObject item = new JSONObject();
			item.put("orderId", orders.getOrderId());
			item.put("customerId",
					(orders.getCustomerId() != null ? String.format("%06d", orders.getCustomerId().getCustomerId())
							: ""));
			item.put("memberId",
					(orders.getMemberId() != null ? String.format("%06d", orders.getMemberId().getMemberId()) : ""));
			item.put("room", orders.getRoom() != null ? orders.getRoom().getRoomId() : "");
			item.put("numberOfPersons", orders.getNumberOfPersons());
			item.put("hours", orders.getHours());
			item.put("orderDate", orderDate);
			item.put("startTime", startTime);
			item.put("endTime", endTime);
			item.put("subTotal", subTotal != null ? subTotal : "");
			item.put("status", status != null ? status.getStatus() : "");
			array.put(item);
			responseBody.put("list", array);
		}
		System.out.println("responseBody.toString() = " + responseBody.toString());
		return responseBody.toString();
	}

	@PostMapping("/orders/find")
	public String findAllTesst(@RequestBody(required = false) String body) {
		JSONObject responseBody = new JSONObject();
		List<Orders> result = orderService.find(body);
		long count = orderService.count(body);
		long countNull = orderService.countOrderDate(body);
		long countTotal = 0;

		if (count - countNull <= 0) {
			countTotal = 0;
		} else {
			countTotal = count - countNull;
		}
		JSONArray array = new JSONArray();
		if (result != null && !result.isEmpty()) {
			for (Orders orders : result) {
				if (orders.getOrderDate() != null && orders.getStartTime() != null) {
					Long orderId = Long.valueOf(orders.getOrderId());
					String orderDate = DatetimeConverter.toString(orders.getOrderDate(), "yyyy-MM-dd");
					String startTime = DatetimeConverter.toString(orders.getStartTime(), "HH:mm");
					String endTime = DatetimeConverter.toString(orders.getEndTime(), "HH:mm");
					OrdersStatusHistory status = oshService.findNewHistory(orders.getOrderId());
					Double subTotal = orderDetailsService.subTotal(orders.getOrderId());
					JSONObject item = new JSONObject();
					item.put("orderId", orderId);
					item.put("memberId",
							orders.getMemberId() != null ? String.format("%06d", orders.getMemberId().getMemberId())
									: "");
					item.put("customerId",
							orders.getCustomerId() != null
									? String.format("%06d", orders.getCustomerId().getCustomerId())
									: "");
					item.put("room", orders.getRoom() != null ? orders.getRoom().getRoomId() : "");
					item.put("size", orders.getRoom() != null ? orders.getRoom().getSize() : "");
					item.put("orderDate", orderDate);
					item.put("hours", orders.getHours());
					item.put("startTime", startTime);
					item.put("endTime", endTime);
					item.put("subTotal", subTotal != null ? subTotal : "");
					item.put("status", status != null ? status.getStatus() : null);
					array.put(item);
				}
			}
		}
		responseBody.put("count", countTotal);
		responseBody.put("list", array);
		return responseBody.toString();
	}

	@PostMapping("/orders/createOrderId")
	public Orders newOrderId() {
		String newOrderId = orderService.generateOrderId();
		Long orderId = Long.valueOf(newOrderId);
		return orderService.createOrderId(orderId);
	}

	@PutMapping("/orders/newOrder/{id}")
	public String newOrders(@PathVariable Long id, @RequestBody String body) {
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
						Orders result = orderService.updateOrders(body);
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

	// 等待
	@PutMapping("/orders/checkIn/{id}")
	public String checkIn(@PathVariable Long id, @RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject(body);
		Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Integer memberId = obj.isNull("memberId") ? null : obj.getInt("memberId");
		Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
		if (customerId == null && memberId == null) {
			JSONObject item = new JSONObject();
			item.put("customerId", "");
			item.put("memberId", "");
			array.put(item);
			responseBody.put("success", false);
			responseBody.put("message", "請填寫客戶資料或是會員編號");
			responseBody.put("list", array);
		} else {
			if (numberOfPersons == null) {
				responseBody.put("success", false);
				responseBody.put("message", "請填寫消費人數");
			} else {
				Orders result = orderService.watting(body);
				if (result == null) {
					responseBody.put("success", false);
					responseBody.put("message", "修改失敗");
				} else {
					responseBody.put("success", true);
					responseBody.put("message", "報到成功，請客戶稍等");
				}
			}
		}
		return responseBody.toString();
	}

	// 等待
	@PutMapping("/orders/inTheRoom/{id}")
	public String inTheRoom(@PathVariable Long id, @RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		JSONObject obj = new JSONObject(body);
		Integer room = obj.isNull("room") ? null : obj.getInt("room");
		if (room == null) {
			responseBody.put("success", false);
			responseBody.put("message", "請選擇包廂!!!");
		} else {
			Orders result = orderService.inTheRoom(body);
			if (result == null) {
				responseBody.put("success", false);
				responseBody.put("message", "選擇失敗");
			} else {
				responseBody.put("success", true);
				responseBody.put("message", "已進入包廂");
			}
		}
		return responseBody.toString();
	}

	@PostMapping("/orders/testNewOrder")
	public String testNewOrder(@RequestBody String body) {
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
						Orders result = orderService.createNewOrder(body);
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
