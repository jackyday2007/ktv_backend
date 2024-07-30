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

import com.ispan.ktv.bean.Customers;
import com.ispan.ktv.service.CustomerService;
import com.ispan.ktv.util.DatetimeConverter;

@RestController
@CrossOrigin
public class CustomersController {
	
	@Autowired
	private CustomerService customerService;
	
	
	@GetMapping("/customer/{idNumber}")
	public String findByCustomerId(@PathVariable(name="idNumber") String idNumber ) {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		Customers customer= customerService.findIdNumber(idNumber);
		if ( customer != null ) {
			JSONObject item = new JSONObject();
			String birth = customer.getBirth() != null ? DatetimeConverter.toString(customer.getBirth(), "yyyy-MM-dd") : null;
			item.put("customerId", String.format("%06d",customer.getCustomerId()));
			item.put("idNumber", customer.getIdNumber());
			item.put("birth", birth);
			item.put("name", customer.getName());
			item.put("phone", String.format("%010d",customer.getPhone()));
			array.put(item);
			responseBody.put("message", "查詢完成");
			responseBody.put("list", array);
		} else {
			if ( idNumber != null) {
				JSONObject item = new JSONObject();
				item.put("customerId", "");
				item.put("idNumber", idNumber);
				item.put("birth", "");
				item.put("name", "");
				item.put("phone", "");
				array.put(item);
				responseBody.put("success", false);
				responseBody.put("message", "查無此證訊息");
				responseBody.put("list", array);
			} else {
				JSONObject item = new JSONObject();
				item.put("customerId", "");
				item.put("idNumber", "");
				array.put(item);
				responseBody.put("success", false);
				responseBody.put("message", "請輸入身分證字號");
				responseBody.put("list", array);
			}

		}
		return responseBody.toString();
	}
	
	@PostMapping("/customer/insert")
	public String insertCustomer(@RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		JSONObject obj = new JSONObject(body);
		String idNumber = obj.isNull("idNumber") ? null : obj.getString("idNumber");
		String name = obj.isNull("name") ? null : obj.getString("name");
		String birth = obj.isNull("birth") ? null : obj.getString("birth");
		Integer phone = obj.isNull("phone") ? null : obj.getInt("phone");
		if ( idNumber == null ) {
			responseBody.put("message", "請輸入身分證字號");
		} else {
			if ( name == null ) {
				responseBody.put("message", "請輸入姓名");
			} else {
				if ( birth == null ) {
					responseBody.put("message", "請輸入生日");
				} else {
					if ( phone == null ) {
						responseBody.put("message", "請輸入電話號碼");
					} else {
						Customers result = customerService.insertCustomer(body);
						if ( result != null ) {
							responseBody.put("message", "新增完成");
						} else {
							responseBody.put("message", "新增失敗");
						}
					}
				}
			}
		}
		return responseBody.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
