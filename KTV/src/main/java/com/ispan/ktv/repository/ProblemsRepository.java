package com.ispan.ktv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.Problems;

public interface ProblemsRepository extends JpaRepository<Problems, Integer>, JpaSpecificationExecutor<Problems> {

	 @Query("SELECT COUNT(p) FROM Problems p WHERE (:problemId IS NULL OR p.problemId = :problemId)")
	long countProblems(@Param("problemId") Integer problemId);
	 
	 @Query("SELECT p FROM Problems p WHERE p.room.roomId = :roomId")
	 public List<Problems> findProblemsByRoom(@Param("roomId") Integer roomId);
	 
	 @Query("SELECT p FROM Problems p WHERE p.status = :status")
	 public List<Problems> findProblemsByStatus(@Param("status") String status);
	 

	 
}
