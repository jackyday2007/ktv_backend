package com.ispan.ktv.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Members;
import com.ispan.ktv.repository.MemberRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;  // 注入 MemberRepository 以執行資料庫操作

    @Autowired
    private JavaMailSender mailSender;  // 注入 JavaMailSender 以發送郵件

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  // 用於密碼加密

    public boolean authenticate(String idNumber, String password) {
        // 根據 ID 查找會員
        Members member = memberRepository.findByIdNumber(idNumber);
        // 檢查會員是否存在，並且使用 BCryptPasswordEncoder 檢查密碼是否正確
        return member != null && passwordEncoder.matches(password, member.getPassword());
    }

    public Members findByIdNumber(String idNumber) {
        // 根據 ID 查找會員
        return memberRepository.findByIdNumber(idNumber);
    }

    public void save(Members member) {
        // 對會員密碼進行加密
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        // 保存會員到資料庫
        memberRepository.save(member);
    }

    public String createPasswordResetToken(Members member) {
        // 生成一個唯一的 token
        String token = UUID.randomUUID().toString();
        member.setResetPasswordToken(token);
        // 設定 token 的過期時間為 1 小時後
        member.setResetPasswordTokenExpiry(new Date(System.currentTimeMillis() + 3600000));  // 1 小時過期
        // 儲存 token 和過期時間到資料庫
        memberRepository.save(member);
        return token;
    }

    public void sendPasswordResetEmail(String email, String resetLink) {
        // 建立發送郵件的消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);  // 收件人電子郵件
        message.setSubject("Password Reset Request");  // 郵件主題
        message.setText("請點擊以下連結來重設密碼:\n" + resetLink);  // 郵件內容
        // 發送郵件
        mailSender.send(message);
    }

    public boolean resetPassword(String token, String newPassword) {
        // 根據 token 查找會員
        Members member = memberRepository.findByResetPasswordToken(token);
        // 檢查 token 是否有效
        if (member == null || member.getResetPasswordTokenExpiry().before(new Date())) {
            return false;  // token 無效
        }
        // 對新密碼進行加密
        member.setPassword(passwordEncoder.encode(newPassword));
        // 清除 token 和過期時間
        member.setResetPasswordToken(null);
        member.setResetPasswordTokenExpiry(null);
        // 儲存更新後的會員資料到資料庫
        memberRepository.save(member);
        return true;  // 密碼重設成功
    }
}
