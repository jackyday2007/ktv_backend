package com.ispan.ktv.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.RoomHistory;

public interface RoomHistoryRepository extends JpaRepository<RoomHistory, Integer> {

    // 根據時間範圍查詢 RoomHistory
    List<RoomHistory> findByDateBetween(Date startDate, Date endDate);

}
