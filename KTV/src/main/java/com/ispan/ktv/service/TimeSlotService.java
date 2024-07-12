package com.ispan.ktv.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ispan.ktv.bean.TimeSlot;
import com.ispan.ktv.repository.TimeSlotRepository;

public class TimeSlotService {
    @Autowired
    private TimeSlotRepository tsr;

    public List<TimeSlot> findByTime(Date wantedStartTime, Date wantedEndTime) {
        
        return tsr.findByStartTimeAmdEndTime(wantedStartTime,wantedEndTime);
    }
}
