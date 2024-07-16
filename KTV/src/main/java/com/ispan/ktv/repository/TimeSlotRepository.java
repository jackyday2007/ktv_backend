package com.ispan.ktv.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.ktv.bean.TimeSlot;
@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
	@Query("FROM TimeSlot t WHERE (t.startTime BETWEEN :wst AND :wet) OR (t.endTime BETWEEN :wst AND :wet)")// )
	List<TimeSlot> findByStartTimeAndEndTime(@Param("wst") Date wantedStartTime ,@Param("wet") Date wantedEndTime);
//	List<TimeSlot> findByStartTimeAmdEndTime( JSONObject obj);
	
}
