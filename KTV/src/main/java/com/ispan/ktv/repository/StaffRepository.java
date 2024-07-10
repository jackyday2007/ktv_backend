package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.Staff;

public interface StaffRepository extends JpaRepository<Staff, Integer>{
    
}
