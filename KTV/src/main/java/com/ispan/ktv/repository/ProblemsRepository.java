package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.Problems;

public interface ProblemsRepository extends JpaRepository<Problems, Integer> {

	 @Query("SELECT COUNT(p) FROM Problems p WHERE (:problemId IS NULL OR p.problemId = :problemId)")
	long countProblems(@Param("problemId") Integer problemId);
	

}
