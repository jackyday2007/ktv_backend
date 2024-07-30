package com.ispan.ktv.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.OrdersStatusHistory;
import com.ispan.ktv.service.OrdersStatusHistoryService;

@RestController
@RequestMapping("/ktvbackend/")
@CrossOrigin
public class OrdersStatusHistoryController {
	
	@Autowired
	private OrdersStatusHistoryService oshService;
	
	@PostMapping("/orders/noCheckIn/{id}")
	public String checkIn(@PathVariable Long id) {
		System.out.println("onCheckIn = " + id);
		JSONObject responseBody = new JSONObject();
		OrdersStatusHistory result = oshService.noCheckIn(id);
		if (result != null) {
			responseBody.put("success", true);
			responseBody.put("message", "已取消");
		}
		return responseBody.toString();
	}
	
	
	
	

}
