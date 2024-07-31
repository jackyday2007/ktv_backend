package com.ispan.ktv.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Checkout;
import com.ispan.ktv.service.CheckoutService;
import com.ispan.ktv.service.OrderDetailsService;

@RestController
@CrossOrigin
public class CheckoutController {
	
	
	@Autowired
	private CheckoutService checkoutService;
	
	@Autowired
	private OrderDetailsService orderDetailsService;
	
	
	@PostMapping("/checkout")
	public String checkout(@RequestBody String body ) {
		JSONObject responseBody = new JSONObject();
		JSONObject obj = new JSONObject(body);
		String pay = obj.isNull("pay") ? null : obj.getString("pay");
		Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
		if ( pay == null ) {
			responseBody.put("message", "請輸入收取金額");
		} else {
			Double subtotal = orderDetailsService.subTotal(orderId);
			Double check = Double.valueOf(pay) - subtotal;
			if ( check < 0 ) {
				responseBody.put("success", false);
				responseBody.put("message","付款金額不足，請確認");
			} else {
				Checkout result = checkoutService.insertCheckout(body);
				if ( result != null ) {
					responseBody.put("success", true);
					responseBody.put("message", "結帳成功，謝謝光臨");
				} else {
					responseBody.put("sucess", false);
					responseBody.put("message", "結帳失敗，");
				}
			}
		}
		return responseBody.toString();
	}
	
	@GetMapping("/checkout/{orderId}")
	public String findcheckoutByOrderId(@PathVariable(name="orderId") Long orderId) {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		if ( orderId != null ) {
			Checkout result = checkoutService.findCheckout(orderId);
			if ( result != null ) {
				JSONObject item = new JSONObject();
				item.put("pay", result.getPay());
				item.put("change", result.getChange());
				array.put(item);
				responseBody.put("list", array);
			}
			
		}
		
		return responseBody.toString();
	}
	

}
