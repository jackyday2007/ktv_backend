package com.ispan.ktv.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.service.OrderDetailsService;

@RestController
@CrossOrigin
public class OrderDetailsController {
	
	
	@Autowired
	OrderDetailsService orderDetailsService;
	
	
	@PostMapping("/orderDetail/new")
	public String createNewOrderDetails(@RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		orderDetailsService.insertDetail(body);
		responseBody.put("success", "新增成功");
		return responseBody.toString();
	}
	
	
	
	

}
