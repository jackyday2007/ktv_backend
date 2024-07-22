package com.ispan.ktv.service;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.TimeSlot;
import com.ispan.ktv.repository.TimeSlotRepository;
import com.ispan.ktv.util.DatetimeConverter;

@Service

public class TimeSlotService {
	@Autowired
	private TimeSlotRepository tsr;

	public List<TimeSlot> findByTime(Date wantedStartTime, Date wantedEndTime) {
		return tsr.findByStartTimeAndEndTime(wantedStartTime, wantedEndTime);

	}

	public TimeSlot create(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String startTime = obj.isNull("startTime") ? null : obj.getString("startTime");
			String endTime = obj.isNull("endTime") ? null : obj.getString("endTime");
			String date = obj.isNull("date") ? null : obj.getString("date");
			TimeSlot insert = new TimeSlot();
			insert.setStartTime(null);
			insert.setStartTime(DatetimeConverter.parse(startTime, "yyyy-MM-dd"));
			insert.setEndTime(null);
			insert.setEndTime(DatetimeConverter.parse(endTime, "yyyy-MM-dd"));
			insert.setDate(null);
			insert.setDate(DatetimeConverter.parse(date, "yyyy-MM-dd"));
			return tsr.save(insert);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List<TimeSlot> findAllTime( String body ) {
		try {
			return tsr.findAll();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
