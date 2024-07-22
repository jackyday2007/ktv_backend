package com.ispan.ktv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.Members;

public interface MemberRepository extends JpaRepository<Members, Integer> {
	 Members findByIdNumber(String idNumber);  // 根據 ID 查找會員
	 Members findByResetPasswordToken(String resetPasswordToken);  // 根據 token 查找會員
	 List<Members> findAll();  // 查詢所有會員
	    
	 @Query(value = "FROM Members WHERE phone = :phone")
	 Optional<Members> findMember(@Param(value = "phone")  String phone);
	    
}
