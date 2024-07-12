package com.ispan.ktv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Staff;
import com.ispan.ktv.service.StaffService;
import com.ispan.ktv.util.DatetimeConverter;

@RestController
public class StaffController {

	@Autowired
	private StaffService ss;
	
	@GetMapping("/staff/find/{id}")
    public String findById(@PathVariable(name = "id") Integer id) {
		System.out.println("id = "+id);
		JSONObject responseBody = new JSONObject();
        JSONArray array = new JSONArray();
        Staff bean = ss.findById(id);
        System.out.println("bean = "+bean);
        if (id != null) {
            String createtime = DatetimeConverter.toString(bean.getCreateTime(), "yy-MM-dd");
            String updateTime = DatetimeConverter.toString(bean.getUpdateTime(), "yy-MM-dd");
            JSONObject item;
			try {
					item = new JSONObject()
					.put("Id",bean.getAccountId())
					.put("name",bean.getAccountName())
					.put("account",bean.getAccount())
					.put("password", bean.getPassword())
					.put("status", bean.getStatus())
					.put("creater", bean.getCreateBy())                         
					.put("createtime", createtime)
					.put("updateBy", bean.getUpdateBy())
					.put("updateTime", updateTime);
					array = array.put(item);
					responseBody.put("list", array);
				} catch (JSONException e) {
					
					e.printStackTrace();
				}                
        }       
        return responseBody.toString(); 
	}
	
	@PostMapping("/staff/create")
    public String create(@RequestBody String body) {
        JSONObject responseBody = new JSONObject();

        JSONObject obj;
		try {
			obj = new JSONObject(body);
			Integer id = obj.isNull("id") ? null : obj.getInt("id");

	        if (id == null) {
	            responseBody.put("success", false);
	            responseBody.put("message", "Id是必要欄位");
	        } else {
	            if (ss.exists(id)) {
	                responseBody.put("success", false);
	                responseBody.put("message", "Id已存在");
	            } else {
	                Staff product = ss.create(body);
	                if (product == null) {
	                    responseBody.put("success", false);
	                    responseBody.put("message", "新增失敗");
	                } else {
	                    responseBody.put("success", true);
	                    responseBody.put("message", "新增成功");
	                }
	            }
	        }
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
        
        return responseBody.toString();
    }
	
}
