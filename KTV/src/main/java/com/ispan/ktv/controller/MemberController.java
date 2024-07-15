package com.ispan.ktv.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Members;
import com.ispan.ktv.service.MemberService;

@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberService memberService;

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
        boolean isAuthenticated = memberService.authenticate(member.getIdNumber(), member.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("登入成功!");
        } else {
            return ResponseEntity.badRequest().body("帳號或密碼有誤");
        }
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Members member = memberService.findByIdNumberAndEmail(request.getIdNumber(), request.getEmail());
        if (member == null) {
            return ResponseEntity.badRequest().body("查無帳號");
        }

        // 生成重置密碼的連結
        String resetToken = memberService.createPasswordResetToken(member);

        // 發送重置密碼的電子郵件
        String resetLink = "http://yourdomain.com/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(member.getEmail(), resetLink);

        return ResponseEntity.ok("已發送重置密碼的連結到您的信箱");
    }
}
