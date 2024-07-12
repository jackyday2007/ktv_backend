package com.ispan.ktv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Members;
import com.ispan.ktv.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public boolean authenticate(String idNumber, String password) {
        Members member = memberRepository.findByIdNumber(idNumber);
        return member != null && member.getPassword().equals(password);
    }

    public Members findByIdNumber(String idNumber) {
        return memberRepository.findByIdNumber(idNumber);
    }

    public void save(Members member) {
        memberRepository.save(member);
    }
}
