package com.ispan.ktv.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ispan.ktv.bean.Members;
import com.ispan.ktv.service.MemberService;
import com.ispan.ktv.util.JwtUtil;
import com.ispan.ktv.util.LoginRequest;
import com.ispan.ktv.util.PasswordResetRequest;
import com.ispan.ktv.util.ResetPasswordRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class MemberController {

    @Autowired
    private MemberService memberService; // 注入 MemberService 以使用其方法

    @Autowired
    private JwtUtil jwtUtil;

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Members member) {
        // 檢查 ID 是否已存在
        if (memberService.findByIdNumber(member.getIdNumber()) != null) {
            return ResponseEntity.badRequest().body("身分證字號已被使用");
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

//    @CrossOrigin
//    @PostMapping("/login")
//    public ResponseEntity<Members> login(@RequestBody Members member) {
//        // 認證會員的 ID 和密碼
//        boolean isAuthenticated = memberService.authenticate(member.getIdNumber(), member.getPassword());
//        if (isAuthenticated) {
//            Members authenticatedMember = memberService.findByIdNumber(member.getIdNumber());
//            return ResponseEntity.ok(authenticatedMember);
//        } else {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Members member) {
        // 认证会员的 ID 和密码
        boolean isAuthenticated = memberService.authenticate(member.getIdNumber(), member.getPassword());
        if (isAuthenticated) {
            // 生成 JWT Token
            String token = jwtUtil.generateToken(member.getIdNumber());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

//    @CrossOrigin
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
//        HttpSession session = request.getSession(false); // 獲取當前 session
//        if (session != null) {
//            session.invalidate(); // 使 session 無效
//        }
//        Cookie cookie = new Cookie("JSESSIONID", null); // 清除 session cookie
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//        return ResponseEntity.ok("登出成功");
//    }
    @CrossOrigin
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 獲取當前 session
        if (session != null) {
            session.invalidate(); // 使 session 無效
        }
        // 如果使用 JWT Token，需要通知客户端删除本地 Token
        return ResponseEntity.ok("登出成功");
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

    @GetMapping("/members/findWithPhone/{phone}")
    public String findMemberWithPhone(@PathVariable(name = "phone") String phone) {
        JSONObject responseBody = new JSONObject();
        JSONArray array = new JSONArray();
        Members members = memberService.findMemberWithPhone(phone);
        if (members != null) {
            JSONObject item = new JSONObject();
            item.put("customerId", "");
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

    @PostMapping("/upload-profile-image/{idNumber}")
    public ResponseEntity<String> uploadProfileImage(@PathVariable String idNumber,
            @RequestParam("file") MultipartFile file) {
        Members member = memberService.findByIdNumber(idNumber);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("會員不存在");
        }

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("沒有上傳檔案");
        }

        try {
            byte[] imageBytes = file.getBytes();
            member.setImage(imageBytes);
            memberService.save(member);

            return ResponseEntity.ok("圖片上傳成功");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上傳圖片時發生錯誤");
        }
    }

    @GetMapping("/profile-image/{idNumber}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable String idNumber) {
        Members member = memberService.findByIdNumber(idNumber);
        if (member == null || member.getImage() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 根據實際圖片格式修改

        return new ResponseEntity<>(member.getImage(), headers, HttpStatus.OK);
    }

}
