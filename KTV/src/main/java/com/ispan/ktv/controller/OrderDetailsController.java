package com.ispan.ktv.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.OrderDetails;
import com.ispan.ktv.service.OrderDetailsService;

@RestController
@CrossOrigin
public class OrderDetailsController {
	
	
	@Autowired
	OrderDetailsService orderDetailsService;
	
	
	
	@PostMapping("/orderDetail/new")
	public String newDetail(@RequestBody String body ) {
		JSONObject responseBody = new JSONObject();
		List<OrderDetails> resutl = orderDetailsService.insertNewDetail(body);
		if ( resutl == null ) {
			responseBody.put("message", "新增失敗");
		} else {
			responseBody.put("message", "新增成功");
		}
		return responseBody.toString();
	}
	
	
	

}
