package com.ispan.ktv.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
	@Query(value="from timeSlot where startTime between :wst and :wet or endTime between :wst and :wet")
	List<TimeSlot> findByStartTimeAmdEndTime(@Param("wst") Date wantedStartTime ,@Param("wet") Date wantedEndTime);
}
