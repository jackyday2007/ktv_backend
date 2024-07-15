package com.ispan.ktv.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.format.annotation.DateTimeFormat;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.TimeSlot;
import com.ispan.ktv.service.TimeSlotService;
// import com.ispan.ktv.util.DatetimeConverter;



@RestController
//@RequestMapping("/timeslot")
//@CrossOrigin
public class timeSlotController {
    @Autowired
    private TimeSlotService ts;

	@GetMapping("/find/time")
    public List<TimeSlot> putMethodName(@RequestParam String wst  , @RequestParam String wet  ) throws 	ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date wantedStartTime =formatter.parse(wst);
        Date wantedEndTime =formatter.parse(wet);
        
        if(wst == null ) {
			wantedStartTime= new Date();
			// System.out.println(wantedStartTime );
			return ts.findByTime(wantedStartTime , wantedEndTime);
        }
		if(wet == null ) {
			wantedEndTime = formatter.parse("2099-12-12");
			// System.out.println(wantedStartTime );
			return ts.findByTime(wantedStartTime , wantedEndTime);
		}
    
		System.out.println(wantedStartTime );
		return ts.findByTime(wantedStartTime , wantedEndTime);
    
    
    }


}
