package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.RoomHistory;

public interface RoomHistoryRepository extends JpaRepository<RoomHistory, Integer> {

}
