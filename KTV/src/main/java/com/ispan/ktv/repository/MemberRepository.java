package com.ispan.ktv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.Members;

public interface MemberRepository extends JpaRepository<Members, Integer> {
	 Members findByIdNumber(String idNumber);  // 根據 ID 查找會員
	    Members findByResetPasswordToken(String resetPasswordToken);  // 根據 token 查找會員
	    List<Members> findAll();  // 查詢所有會員
}
