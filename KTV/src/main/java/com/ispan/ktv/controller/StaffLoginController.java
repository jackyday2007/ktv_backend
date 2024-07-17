package com.ispan.ktv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Staff;
import com.ispan.ktv.service.StaffService;
import com.ispan.ktv.util.DatetimeConverter;

import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

@RestController
@CrossOrigin
public class StaffLoginController {
    // @Autowired
    // private JsonWebTokenUtility jsonWebTokenUtility;
    @Autowired
    private StaffService staffService;
        @PostMapping("/login")
    public String login(@RequestBody String body, HttpSession hs) {
        JSONObject responseBody = new JSONObject();


        JSONObject obj = new JSONObject(body);
        String account = obj.isNull("account") ? null : obj.getString("account");
        String password = obj.isNull("password") ? null : obj.getString("password");

        if (account == null || account.length() == 0 || password == null || password.length() == 0) {
            responseBody.put("success", false);
            responseBody.put("message", "請輸入帳號及密碼");
            return responseBody.toString();

        }
        Staff bean = staffService.login(account, password);
        if (bean == null) {
            responseBody.put("success", false);
            responseBody.put("message", "請檢查帳號及密碼");
            return responseBody.toString();
        } else {
            hs.setAttribute("user", bean);
            responseBody.put("success", true);
            responseBody.put("message", "登入成功");
            
            // String birth = DatetimeConverter.toString(bean.getBirth(), "yyyy-MM-dd");
            // JSONObject user = new JSONObject()
            //                         .put("custid", bean.getCustid())
            //                         .put("email", bean.getEmail())
            //                         .put("birth", birth);

            // responseBody.put("user", bean.getEmail());
            
            
        }

         return responseBody.toString();
    }
}
