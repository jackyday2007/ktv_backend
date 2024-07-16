package com.ispan.ktv.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import com.ispan.ktv.bean.TimeSlot;
import com.ispan.ktv.repository.TimeSlotRepository;
@Service

public class TimeSlotService {
    @Autowired
    private TimeSlotRepository tsr;

    public List<TimeSlot> findByTime( Date wantedStartTime, Date wantedEndTime ) {
        return tsr.findByStartTimeAndEndTime(wantedStartTime, wantedEndTime);
        
    }
//    	try {
//			JSONObject obj = new JSONObject(json);
//			return tsr.findByStartTimeAmdEndTime(wantedStartTime, wantedEndTime);
////			return tsr.findByStartTimeAmdEndTime(obj);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return null;
//    }
//   
}
