package com.ispan.ktv.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.RoomHistory;

public interface RoomHistoryRepository extends JpaRepository<RoomHistory, Integer> {
	
	// 根據時間範圍查詢 RoomHistory
    List<RoomHistory> findByDateBetween(Date startDate, Date endDate);
//    
//    @Query("SELECT h FROM RoomHistory h JOIN h.room r " +
//            "WHERE h.date BETWEEN :startDate AND :endDate")
//     List<RoomHistory> findRoomHistoryByTimeRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
//    
    
}
