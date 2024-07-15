package com.ispan.ktv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Members;
import com.ispan.ktv.repository.MemberRepository;

@Service
public class MemberService {

    // 依賴注入 MemberRepository，通過它可以訪問資料庫中的會員數據
    @Autowired
    private MemberRepository memberRepository;

    // 用於密碼加密的 BCryptPasswordEncoder 實例
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 驗證會員的 ID 和密碼是否正確
     * @param idNumber 會員的 ID Number
     * @param password 會員的密碼
     * @return 如果會員存在且密碼匹配則返回 true，否則返回 false
     */
    public boolean authenticate(String idNumber, String password) {
        // 根據 ID 查找會員
        Members member = memberRepository.findByIdNumber(idNumber);
        // 檢查會員是否存在，並且使用 BCryptPasswordEncoder 檢查密碼是否正確
        return member != null && passwordEncoder.matches(password, member.getPassword());
    }

    /**
     * 根據 ID Number 查找會員
     * @param idNumber 會員的 ID Number
     * @return 返回符合 ID 的會員對象，若找不到則返回 null
     */
    public Members findByIdNumber(String idNumber) {
        // 根據 ID 查找會員
        return memberRepository.findByIdNumber(idNumber);
    }

    /**
     * 保存會員信息
     * @param member 需要保存的會員對象
     */
    public void save(Members member) {
        // 對會員密碼進行加密
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        // 保存會員到資料庫
        memberRepository.save(member);
    }
}
