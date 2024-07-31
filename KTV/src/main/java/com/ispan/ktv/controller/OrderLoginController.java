package com.ispan.ktv.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Staff;
import com.ispan.ktv.service.StaffService;
import com.ispan.ktv.util.JwtUtil;

import jakarta.servlet.http.HttpSession;

@RestController

@CrossOrigin(origins = "http://localhost:5173/secure/login")
public class OrderLoginController {
	
	@Autowired
	StaffService staffService;
	
	 @Autowired
	 private JwtUtil jwtUtil;
	 
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
			 //JSONObject user = new JSONObject().put("account", bean.getAccount());
			 // 使用 JwtUtil 的 generateToken 方法
			  // 將 account 轉換為 String
		        String accountStr = account.toString();
		        // 使用 String 類型的 account
		        String token = jwtUtil.generateToken(accountStr); // 使用帳號來生成 Token
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
