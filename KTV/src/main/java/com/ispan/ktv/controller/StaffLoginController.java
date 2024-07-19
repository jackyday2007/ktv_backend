package com.ispan.ktv.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Staff;
import com.ispan.ktv.service.StaffService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class StaffLoginController {
    // @Autowired
    // private JsonWebTokenUtility jsonWebTokenUtility;
    @Autowired
    private StaffService staffService;
        @PostMapping("/staff/login")
        public String login(@RequestBody String body, HttpSession hs) {
            JSONObject responseBody = new JSONObject();

            JSONObject obj = new JSONObject(body);
            Integer account = obj.isNull("account") ? null : obj.getInt("account");
            String password = obj.isNull("password") ? null : obj.getString("password");

            if (account == null  || password == null || password.length() == 0) {
                responseBody.put("success", false);
                responseBody.put("message", "請輸入帳號及密碼");
                return responseBody.toString();

            }
            Staff bean = staffService.login(account, password);
            if (bean == null) {
                responseBody.put("success", false);
                responseBody.put("message", "請檢查帳號及密碼");
                return responseBody.toString();
            } else if (bean.getStatus() == 0) {
                responseBody.put("success", false);
                responseBody.put("message", "此帳已停止使用");
                return responseBody.toString();
            } else {
                hs.setAttribute("user", bean);
                // hs.setAttribute("pasd", bean);
                responseBody.put("success", true);
                responseBody.put("message", "登入成功");

                
                // JSONObject user = new JSONObject()
                //                         .put("custid", bean.getAccountName())
                //         .put("account", bean.getAccount())
                //                         .put("password", bean.getPassword());
                                        
                
                responseBody.put("user", bean.getAccount());
                // responseBody.put("pasd", bean.getPassword());
            }

            return responseBody.toString();
        }
        @PutMapping("/staff/updatePassword")
        public String updatePassword(@RequestBody String body) {
            JSONObject responseBody = new JSONObject();
            JSONObject obj = new JSONObject(body);
            Integer account = obj.isNull("account") ? null : obj.getInt("account");
            String oldPassword = obj.isNull("oldPassword") ? null : obj.getString("oldPassword");
            String newPassword = obj.isNull("newPassword") ? null : obj.getString("newPassword");
            String checkPassword = obj.isNull("checkPassword") ? null : obj.getString("checkPassword");
            if (account == null || oldPassword == null || oldPassword.length() == 0) {
                responseBody.put("success", false);
                responseBody.put("message", "請輸入帳號及密碼");
                return responseBody.toString();
            }
            if (!newPassword.equals(checkPassword) && newPassword.length() != 0 && checkPassword.length() != 0) {
                responseBody.put("success", false);
                responseBody.put("message", "請輸入一致的新密碼");
                return responseBody.toString();
            }
            Staff bean = staffService.login(account, oldPassword);
            if (bean == null) {
                responseBody.put("success", false);
                responseBody.put("message", "請檢查帳號及密碼");
                return responseBody.toString();
            } else if (bean.getStatus() == 0) {
                responseBody.put("success", false);
                responseBody.put("message", "此帳已停止使用");
                return responseBody.toString();
            }else {
                staffService.changePassword(body);
                responseBody.put("success", true);
                responseBody.put("message", "修改成功");
            }
            return responseBody.toString();
        }
}
