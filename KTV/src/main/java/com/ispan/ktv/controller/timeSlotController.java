package com.ispan.ktv.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.TimeSlot;
import com.ispan.ktv.service.TimeSlotService;
// import com.ispan.ktv.util.DatetimeConverter;



@RestController
@RequestMapping("/timeslot")
public class timeSlotController {
    @Autowired
    private TimeSlotService ts;

    @PutMapping("path")
    public String putMethodName( @RequestBody String entity) throws Exception{
    	JSONObject responseBody = new JSONObject();
    	JSONArray array = new JSONArray();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
        JSONObject obj = new JSONObject(entity);
        String wst = obj.isNull("wst") ? null : obj.getString("wst");
        String wet = obj.isNull("wet") ? null : obj.getString("wet");
        Date wantedStartTime = formatter.parse(wst);
        Date wantedEndTime = formatter.parse(wet);
        List<TimeSlot> result = ts.findByTime(wantedStartTime, wantedEndTime);
        if (result != null && !result.isEmpty()) {
            for (TimeSlot bean : result) {
                
                JSONObject item = new JSONObject()
                .put("id",bean.getTimeSlotId())
                .put("StartTime",bean.getStartTime())                                       
                .put("EndTime", bean.getEndTime())                         
                .put("Date", bean.getDate());                        
                array = array.put(item);
            }
        }
//        long count = ts.count(entity);
//        responseBody.put("count", count);
        responseBody.put("list", array);
        return responseBody.toString();
    }

}
