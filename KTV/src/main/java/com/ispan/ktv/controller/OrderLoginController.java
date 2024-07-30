package com.ispan.ktv.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Staff;
import com.ispan.ktv.service.StaffService;
import com.ispan.ktv.util.JsonWebTokenUtility;

import jakarta.servlet.http.HttpSession;

@RestController

@CrossOrigin
public class OrderLoginController {
	
	@Autowired
	StaffService staffService;
	
	 @Autowired
	 private JsonWebTokenUtility jsonWebTokenUtility;
	 
	 @PostMapping("/orders/login")
	 public String login(@RequestBody String body, HttpSession httpsession) {
		 JSONObject responseBody = new JSONObject();
		 JSONObject obj = new JSONObject(body);
		 Integer account = obj.isNull("account") ? null : obj.getInt("account");
		 String password = obj.isNull("password") ? null : obj.getString("password");
		 if (account == null || password == null || password.length() == 0) {
			 responseBody.put("success", false);
			 responseBody.put("message", "請輸入帳號密碼");
			 return responseBody.toString();
		 }
		 Staff bean = staffService.login(account, password);
		 if (bean == null) {
			 responseBody.put("message", "登入失敗，請確認您的帳號密碼");
			 responseBody.put("success", false);
		 } else {
			 httpsession.setAttribute("user", bean);
			 responseBody.put("message", "登入成功，歡迎使用KTV櫃台系統");
			 responseBody.put("success", true);
			 JSONObject user = new JSONObject().put("account", bean.getAccount());
			 String token = jsonWebTokenUtility.createEncryptedToken( user.toString() , 1L*60*1000);
			 responseBody.put("token", token);
			 responseBody.put("user", bean.getAccount());
		 }
		 return responseBody.toString();
	 }
	 
	 @PostMapping("/orders/logout")
	 public String logout(HttpSession httpSession) {
		 JSONObject responseBody = new JSONObject();
		 httpSession.invalidate();
		 responseBody.put("success", true);
		 responseBody.put("message", "登出成功");
		 return responseBody.toString();
	 }
	
	

}
