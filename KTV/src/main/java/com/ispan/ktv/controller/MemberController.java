package com.ispan.ktv.controller;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Members;
import com.ispan.ktv.service.MemberService;
import com.ispan.ktv.util.PasswordResetRequest;
import com.ispan.ktv.util.ResetPasswordRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberService memberService; // 注入 MemberService 以使用其方法

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Members member) {
        // 檢查 ID 是否已存在
        if (memberService.findByIdNumber(member.getIdNumber()) != null) {
            return ResponseEntity.badRequest().body("ID Number已被使用");
        }

        // 設定密碼和其他屬性
        member.setStatus(1); // 設定狀態為啟用
        member.setCreateTime(new Date()); // 設定創建時間

        // 將密碼加密後再存入資料庫
        String encryptedPassword = memberService.encryptPassword(member.getPassword());
        member.setPassword(encryptedPassword);

        memberService.save(member); // 儲存會員

        return ResponseEntity.ok("註冊成功!");
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<Members> login(@RequestBody Members member) {
        // 認證會員的 ID 和密碼
        boolean isAuthenticated = memberService.authenticate(member.getIdNumber(), member.getPassword());
        if (isAuthenticated) {
            Members authenticatedMember = memberService.findByIdNumber(member.getIdNumber());
            return ResponseEntity.ok(authenticatedMember);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @CrossOrigin
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 清除 session 或 token
        // 具體實作根據您的需求
        return ResponseEntity.ok("Logout successful");
    }

    @CrossOrigin
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest request) {
        // 根據 ID 查找會員
        Members member = memberService.findByIdNumber(request.getIdNumber());
        if (member == null || !member.getEmail().equals(request.getEmail())) {
            return ResponseEntity.badRequest().body("查無帳號"); // ID 或 Email 錯誤
        }

        // 生成重設密碼的 token 和連結
        String token = memberService.createPasswordResetToken(member);
        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        // 發送包含重設密碼連結的郵件
        memberService.sendPasswordResetEmail(member.getEmail(), resetLink);

        return ResponseEntity.ok("重設密碼的連結已發送至您的信箱!");
    }

    @CrossOrigin
    @PostMapping("/reset-password")

    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {

        boolean isSuccess = memberService.resetPassword(request.getToken(), request.getNewPassword());

        if (isSuccess) {

            return ResponseEntity.ok("密碼重設成功");

        } else {

            return ResponseEntity.badRequest().body("無效的 token");

        }

    }

    @CrossOrigin
    @GetMapping("/members/{idNumber}")
    public ResponseEntity<Members> getMemberByIdNumber(@PathVariable String idNumber) {
        Members member = memberService.findByIdNumber(idNumber);
        if (member != null) {
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin
    @GetMapping("/members")
    public ResponseEntity<List<Members>> getAllMembers() {
        List<Members> members = memberService.findAllMembers();
        return ResponseEntity.ok(members);
    }

    @CrossOrigin
    @PutMapping("/members/{idNumber}")
    public ResponseEntity<String> updateMember(@PathVariable String idNumber, @RequestBody Members member) {
        Members existingMember = memberService.findByIdNumber(idNumber);
        if (existingMember != null) {
            existingMember.setMemberName(member.getMemberName());
            existingMember.setPhone(member.getPhone());
            existingMember.setBirth(member.getBirth());
            existingMember.setEmail(member.getEmail());
            existingMember.setUpdateTime(new Date()); // 更新時間
            memberService.save(existingMember);
            return ResponseEntity.ok("會員資料更新成功");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // // 查詢所有會員
    // @GetMapping("/members")
    // public ResponseEntity<List<Members>> getAllMembers() {
    // List<Members> membersList = memberService.findAllMembers();
    // if (membersList.isEmpty()) {
    // return ResponseEntity.noContent().build();
    // } else {
    // return ResponseEntity.ok(membersList);
    // }
    // }

    @GetMapping("/members/findWithPhone/{phone}")
    public String findMemberWithPhone(@PathVariable(name = "phone") String phone) {
        JSONObject responseBody = new JSONObject();
        JSONArray array = new JSONArray();
        Members members = memberService.findMemberWithPhone(phone);
        if (members != null) {
            JSONObject item = new JSONObject();
            item.put("memberId", String.format("%06d", members.getMemberId()));
            item.put("phone", phone);
            array.put(item);
            responseBody.put("message", "查詢完成");
            responseBody.put("list", array);
        } else {
            responseBody.put("message", "查無此會員");
        }
        return responseBody.toString();
    }

    @CrossOrigin
    @DeleteMapping("/members/{idNumber}")
    public ResponseEntity<String> deleteMember(@PathVariable String idNumber) {
        Members existingMember = memberService.findByIdNumber(idNumber);
        if (existingMember != null) {
            memberService.delete(existingMember);
            return ResponseEntity.ok("會員資料刪除成功");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
