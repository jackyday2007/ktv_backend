package com.ispan.ktv.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.OrderMenus;
import com.ispan.ktv.service.OrderMenuService;

@RestController
@CrossOrigin
public class OrderMenusController {
	
	@Autowired
	OrderMenuService orderMenuService;
	
	
	@PostMapping("/orderMenu/allMenu")
	public String allMenu(@RequestBody(required = false) String body  ) {
		JSONObject responseBody = new JSONObject();
		List<OrderMenus> result = orderMenuService.find(body);
		JSONArray array = new JSONArray();
		if ( result != null && !result.isEmpty() ) {
			for ( OrderMenus orderMenus : result ) {
				JSONObject item = new JSONObject();
				item.put("itemId", orderMenus.getItemId());
				item.put("category", orderMenus.getCategory());
				item.put("itemName", orderMenus.getItemName());
				item.put("capacity", orderMenus.getCapacity());
				item.put("price", orderMenus.getPrice());
				item.put("status", orderMenus.getStatus());
				array.put(item);
			}
		}
		responseBody.put("list", array);
		return responseBody.toString();
	}
	
	

}
