package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.Problems;

public interface ProblemsRepository extends JpaRepository<Problems, Integer> {

}
