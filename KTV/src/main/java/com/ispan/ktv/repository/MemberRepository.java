package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.Members;

public interface MemberRepository extends JpaRepository<Members, Integer> {
    Members findByIdNumber(String idNumber);
}
