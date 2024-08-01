package com.ispan.ktv.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Staff;
import com.ispan.ktv.service.StaffService;
import com.ispan.ktv.util.DatetimeConverter;



@RestController
@CrossOrigin
public class StaffController {

	@Autowired
	private StaffService ss;

	
	@PostMapping("/staff/findAccount")
	public String findByAccount(@RequestBody String body) throws JSONException{
		
		JSONObject obj = new JSONObject(body);
		Integer account = obj.isNull("account") ? null : obj.getInt("account");
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		Staff bean = ss.findByAccount(account);
		System.out.println("bean = " + bean);
		System.out.println("account"+account);
		if (account != null) {
			System.out.println("account"+account);
			JSONObject item;
			
				item = new JSONObject()
				.put("Id", bean.getAccountId())
				.put("name", bean.getAccountName())
				.put("account", bean.getAccount())
				.put("password", bean.getPassword())
				.put("status", bean.getStatus());
				array = array.put(item);
				responseBody.put("list", array);
			
		}
		return responseBody.toString();
	}
	

	@PostMapping("/staff/findbyname")
	public String findByName(@RequestBody String name) {
		
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			JSONObject obj = new JSONObject(name);
			String accountName = obj.isNull("name") ? null : obj.getString("name");
			List<Staff> result = ss.findByName(accountName);
			if (result != null && !result.isEmpty()) {
				for (Staff bean : result) {
					String createtime = DatetimeConverter.toString(bean.getCreateTime(), "yy-MM-dd");
					String updateTime = DatetimeConverter.toString(bean.getUpdateTime(), "yy-MM-dd");
					
						JSONObject item = new JSONObject()
								.put("id", bean.getAccountId())
								.put("name", bean.getAccountName())
								.put("account", bean.getAccount())
								.put("password", bean.getPassword())
								.put("status", bean.getStatus())
								.put("creater", bean.getCreateBy())
								.put("createtime", createtime)
								.put("updateBy", bean.getUpdateBy())
								.put("updateTime", updateTime);
						array = array.put(item);
						responseBody.put("list", array);
						
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return  responseBody.toString();
	}

	@PostMapping("/staff/findall")
	public String findall(@RequestBody(required = false) String body) {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		List<Staff> result = ss.find(body);
		System.out.println("result"+result);
		Long count = ss.count(body);
		if (result != null && !result.isEmpty()) {
			for (Staff bean : result) {
				String createtime = DatetimeConverter.toString(bean.getCreateTime(), "yy-MM-dd");
				String updateTime = DatetimeConverter.toString(bean.getUpdateTime(), "yy-MM-dd");
				try {
					JSONObject item = new JSONObject()
					.put("id", bean.getAccountId())
					.put("name", bean.getAccountName())
					.put("account", bean.getAccount()).put("password", bean.getPassword())
					.put("status", bean.getStatus()).put("creater", bean.getCreateBy())
					.put("createtime", createtime).put("updateBy", bean.getUpdateBy())
					.put("updateTime", updateTime);
					array = array.put(item);
					responseBody.put("count", count);
					responseBody.put("list", array);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		return responseBody.toString();
	}

	@GetMapping("/staff/find/{id}")
	public String findById(@PathVariable(name = "id") Integer id) {
		// System.out.println("id = " + id);
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		Staff bean = ss.findById(id);
		// System.out.println("bean = " + bean);
		if (id != null) {
			String createtime = DatetimeConverter.toString(bean.getCreateTime(), "yy-MM-dd");
			String updateTime = DatetimeConverter.toString(bean.getUpdateTime(), "yy-MM-dd");
			JSONObject item;
			try {
				item = new JSONObject().put("Id", bean.getAccountId()).put("name", bean.getAccountName())
						.put("account", bean.getAccount()).put("password", bean.getPassword())
						.put("status", bean.getStatus()).put("creater", bean.getCreateBy())
						.put("createtime", createtime).put("updateBy", bean.getUpdateBy())
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
			Integer account = obj.isNull("account") ? null : obj.getInt("account");

			if (account == null) {
				responseBody.put("success", false);
				responseBody.put("message", "account是必要欄位");
			} else {
				if (ss.exists(account)) {
					responseBody.put("success", false);
					responseBody.put("message", "account已存在");
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

	@PutMapping("/staff/update/{pk}")
	public String modify(@PathVariable(name = "pk") Integer id, @RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		try {
			if (id == null) {
				responseBody.put("success", false);
				responseBody.put("message", "Id是必要欄位");
			} else {
				if (!ss.existsId(id)) {
					responseBody.put("success", false);
					responseBody.put("message", "Id不存在");
				} else {
					Staff product = ss.Update(body);
					if (product == null) {
						responseBody.put("success", false);
						responseBody.put("message", "修改失敗");
					} else {
						responseBody.put("success", true);
						responseBody.put("message", "修改成功");
					}
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return responseBody.toString();

	}
}
