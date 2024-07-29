package com.ispan.ktv.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.OrderDetails;
import com.ispan.ktv.service.OrderDetailsService;
import com.ispan.ktv.util.DatetimeConverter;

@RestController
@CrossOrigin
public class OrderDetailsController {

	@Autowired
	OrderDetailsService orderDetailsService;

	@PostMapping("/orderDetail/new")
	public String newDetail(@RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		List<OrderDetails> resutl = orderDetailsService.insertNewDetail(body);
		if (resutl == null) {
			responseBody.put("message", "新增失敗");
		} else {
			responseBody.put("message", "新增成功");
		}
		return responseBody.toString();
	}

	@GetMapping("/orderDetail/{orderId}")
	public String orderDetailsList(@PathVariable(name = "orderId") Long orderId) {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		List<OrderDetails> orderDetails = orderDetailsService.orderDetailsList(orderId);
		if (orderDetails != null && !orderDetails.isEmpty()) {
			for (OrderDetails orderDetail : orderDetails) {
				JSONObject item = new JSONObject();
				item.put("id", orderDetail.getId());
				item.put("orderDetailId", orderDetail.getOrderDetailId());
				item.put("orderId", orderDetail.getOrderId().getOrderId());
				item.put("item", orderDetail.getItem());
				Double price = Double.valueOf(orderDetail.getQuantity()) != null
						? orderDetail.getSubTotal() / Double.valueOf(orderDetail.getQuantity())
						: 1;
				item.put("price", price);
				item.put("quantity", orderDetail.getQuantity());
				item.put("subTotal", orderDetail.getSubTotal());
				item.put("createTime", DatetimeConverter.toString(orderDetail.getCreateTime(), "HH:mm"));
				array.put(item);
			}
			responseBody.put("list", array);
		} else {
			responseBody.put("message", "查詢失敗");
		}
		return responseBody.toString();
	}

}
