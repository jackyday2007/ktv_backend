package com.ispan.ktv.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Members;
import com.ispan.ktv.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JavaMailSender mailSender;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean authenticate(String idNumber, String password) {
        Members member = memberRepository.findByIdNumber(idNumber);
        return member != null && passwordEncoder.matches(password, member.getPassword());
    }

    public Members findByIdNumber(String idNumber) {
        return memberRepository.findByIdNumber(idNumber);
    }

    public void save(Members member) {
        memberRepository.save(member);
    }

    public String createPasswordResetToken(Members member) {
        String token = UUID.randomUUID().toString();
        member.setResetPasswordToken(token);
        member.setResetPasswordTokenExpiry(new Date(System.currentTimeMillis() + 3600000)); // 1 小時過期
        memberRepository.save(member);
        return token;
    }

    public void sendPasswordResetEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("請點擊以下連結來重設密碼:\n" + resetLink);
        mailSender.send(message);
    }

    public boolean resetPassword(String token, String newPassword) {
        Members member = memberRepository.findByResetPasswordToken(token);
        if (member == null || member.getResetPasswordTokenExpiry().before(new Date())) {
            return false;
        }
        member.setPassword(passwordEncoder.encode(newPassword));
        member.setResetPasswordToken(null);
        member.setResetPasswordTokenExpiry(null);
        memberRepository.save(member);
        return true;
    }

    public List<Members> findAllMembers() {
        return memberRepository.findAll();
    }

    public Members findMemberWithPhone(String phone) {
        if (phone != null) {
            Optional<Members> result = memberRepository.findMember(phone);
            if (result.isPresent()) {
                return result.get();
            }
        }
        return null;
    }

    public void delete(Members member) {
        memberRepository.delete(member);
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
