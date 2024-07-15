package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.Members;

public interface MemberRepository extends JpaRepository<Members, Integer> {
	 Members findByIdNumber(String idNumber);  // 根據 ID 查找會員
	    Members findByResetPasswordToken(String resetPasswordToken);  // 根據 token 查找會員
}
