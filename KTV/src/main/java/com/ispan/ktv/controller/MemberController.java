package com.ispan.ktv.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Members;
import com.ispan.ktv.service.MemberService;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberService memberService;  // 注入 MemberService 以使用其方法

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Members member) {
        // 檢查 ID 是否已存在
        if (memberService.findByIdNumber(member.getIdNumber()) != null) {
            return ResponseEntity.badRequest().body("ID Number已被使用");
        }
        // 設定密碼和其他屬性
        member.setStatus(1);  // 設定狀態為啟用
        member.setCreateTime(new Date());  // 設定創建時間
        memberService.save(member);  // 儲存會員

        return ResponseEntity.ok("註冊成功!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Members member) {
        // 認證會員的 ID 和密碼
        boolean isAuthenticated = memberService.authenticate(member.getIdNumber(), member.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("登入成功!");
        } else {
            return ResponseEntity.badRequest().body("帳號或密碼有誤");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest request) {
        // 根據 ID 查找會員
        Members member = memberService.findByIdNumber(request.getIdNumber());
        if (member == null || !member.getEmail().equals(request.getEmail())) {
            return ResponseEntity.badRequest().body("查無帳號");  // ID 或 Email 錯誤
        }

        // 生成重設密碼的 token 和連結
        String token = memberService.createPasswordResetToken(member);
        String resetLink = "http://localhost:8080/ktv-app/reset-password?token=" + token;

        // 發送包含重設密碼連結的郵件
        memberService.sendPasswordResetEmail(member.getEmail(), resetLink);

        return ResponseEntity.ok("重設密碼的連結已發送至您的郵箱");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        // 使用 token 重設密碼
        boolean isSuccess = memberService.resetPassword(request.getToken(), request.getNewPassword());
        if (isSuccess) {
            return ResponseEntity.ok("密碼重設成功");
        } else {
            return ResponseEntity.badRequest().body("無效的 token");
        }
    }
}

