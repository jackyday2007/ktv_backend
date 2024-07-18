package com.ispan.ktv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.Staff;


public interface StaffRepository extends JpaRepository<Staff, Integer> {
    @Query(value="from Staff where accountName like :name")
    // public long countByName(String name);
    List<Staff> findByName(@Param("name") String name);
    @Query(value="from Staff where account = :username")
    Optional<Staff> findByAccount(@Param("username") Integer account);
}
