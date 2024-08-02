package com.ispan.ktv.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.RoomHistory;
import com.ispan.ktv.bean.Rooms;

public interface RoomHistoryRepository extends JpaRepository<RoomHistory, Integer> {

	@Query("FROM RoomHistory WHERE date = :date AND room = :room ORDER BY createTime DESC ")
	List<RoomHistory> findRoomHistoryWhithDateAndRoom(@Param("date") Date date, @Param("room") Rooms room);
	
	@Query("SELECT r FROM RoomHistory r WHERE r.date = :date AND r.room = :room ORDER BY r.createTime DESC")
	RoomHistory findLatestRoomHistoryByDateAndRoom(@Param("date") Date date, @Param("room") Rooms room);

	// 根據時間範圍查詢 RoomHistory
	List<RoomHistory> findByDateBetween(Date startDate, Date endDate);
	
}
