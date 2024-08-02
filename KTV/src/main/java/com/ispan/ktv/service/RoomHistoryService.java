package com.ispan.ktv.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.RoomHistory;
import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.repository.RoomHistoryRepository;
import com.ispan.ktv.repository.RoomsRepository;
import com.ispan.ktv.util.DatetimeConverter;

@Service
public class RoomHistoryService {
	
	@Autowired
	private RoomsRepository roomsRepo;
	
	@Autowired
	private RoomHistoryRepository roomHistoryRepo;
	
	
	
	public boolean checkRoomAvailable(String body) {
		JSONObject obj = new JSONObject(body);
		Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
		String orderDate = obj.isNull("orderDate") ? null : obj.getString("orderDate");
		Integer hours = obj.isNull("hours") ? null : obj.getInt("hours");
		String startTime = obj.isNull("startTime") ? null : obj.getString("startTime");
		String endTimeString = null;
		if (startTime != null && hours != null) {
			LocalTime start = LocalTime.parse(startTime);
			LocalTime end = start.plusHours(hours);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			endTimeString = end.format(formatter);
		}
		Date date = convertStringToDate(orderDate);
		List<Rooms> rooms = null;
		if ( numberOfPersons > 0 && numberOfPersons <= 6 ) {
			rooms = roomsRepo.findRoomSize("小");
		} else if ( numberOfPersons >= 7 && numberOfPersons <= 10 ) {
			rooms = roomsRepo.findRoomSize("中");
		} else {
			rooms = roomsRepo.findRoomSize("大");
		}
		for ( Rooms roomId : rooms ) {
			List<RoomHistory> histories = roomHistoryRepo.findRoomHistoryWhithDateAndRoom(date, roomId);
			if ( isRoomAvailable(histories, startTime, endTimeString) ) {
				return true;
			}
		}
		return false;
	}
	
	// 時間區段判斷 Start
    private boolean isRoomAvailable(List<RoomHistory> histories, String startTime, String endTime) {
        for (RoomHistory history : histories) {
            // 獲取已存在的預訂的開始時間和結束時間
            String existingStartTime = DatetimeConverter.toString(history.getStartTime(), "HH:mm");
            String existingEndTime = DatetimeConverter.toString(history.getEndTime(), "HH:mm");

            // 檢查時間區間是否重疊
            if (isTimeOverlap(startTime, endTime, existingStartTime, existingEndTime)) {
            	if ("取消預約".equals(history.getStatus())) {
            		return true;
            	} else {
            		// 時間衝突，房間不可用
                    return false;
            	}
            }
        }
        // 時間區間內沒有衝突，房間可用
        return true; 
    }
	

    private boolean isTimeOverlap(String startTime1, String endTime1, String startTime2, String endTime2) {
        // 轉換時間字符串為時間戳（可以選擇其他格式
        long start1 = parseTimeToTimestamp(startTime1);
        long end1 = parseTimeToTimestamp(endTime1);
        long start2 = parseTimeToTimestamp(startTime2);
        long end2 = parseTimeToTimestamp(endTime2);

        // 檢查是否重疊
        return start1 < end2 && end1 > start2;
    }
    
    private long parseTimeToTimestamp(String time) {
        // 示例：將時間字符串轉換為時間戳（毫秒）
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            return dateFormat.parse(time).getTime();
        } catch (ParseException e) {
            throw new RuntimeException("时间格式错误: " + time, e);
        }
    }

    // 時間區段判斷 End
    
	private static Date convertStringToDate(String dateString) {
        // 定义日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // 设置为严格解析模式

        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

}

