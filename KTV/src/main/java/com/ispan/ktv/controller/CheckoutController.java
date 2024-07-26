package com.ispan.ktv.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Checkout;
import com.ispan.ktv.service.CheckoutService;

@RestController
@CrossOrigin
public class CheckoutController {
	
	
	@Autowired
	CheckoutService checkoutServicep;
	
	
	@PostMapping("/checkout/")
	public String checkout(@RequestBody String body ) {
		JSONObject responseBody = new JSONObject();
		JSONObject obj = new JSONObject();
		Double pay = obj.isNull("pay") ? null : obj.getDouble("pay");
		if ( pay == null ) {
			responseBody.put("message", "請輸入收取金額");
		} else {
			Checkout result = checkoutServicep.insertCheckout(body);
			if ( result != null ) {
				responseBody.put("sucess", true);
				responseBody.put("message", "結帳成功，謝謝光臨");
			} else {
				responseBody.put("sucess", false);
				responseBody.put("message", "結帳失敗，");
			}
		}
		return responseBody.toString();
	}
	
	

}
