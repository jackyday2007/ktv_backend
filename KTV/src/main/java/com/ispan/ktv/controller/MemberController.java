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
            return ResponseEntity.badRequest().body("ID Number already exists");
        }

        // 設定密碼和其他屬性
        member.setStatus(1);  // 設定狀態為啟用
        member.setCreateTime(new Date());  // 設定創建時間
        memberService.save(member);

        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Members member) {
        boolean isAuthenticated = memberService.authenticate(member.getIdNumber(), member.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.badRequest().body("Invalid ID Number or Password");
        }
    }
}
