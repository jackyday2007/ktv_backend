package com.ispan.ktv.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.service.RoomHistoryService;

@RestController
@CrossOrigin
public class RoomHistoryController {
	
	@Autowired
	RoomHistoryService roomHistoryService;
	
	@PostMapping("/roomCheck")
	public String checkRoom(@RequestBody String body ) {
		System.out.println("checkRoom = " + body);
		JSONObject responseBody = new JSONObject();
		JSONObject obj = new JSONObject();
		boolean result = roomHistoryService.checkRoomAvailable(body);
		if ( result== false ) {
			responseBody.put("success", false);
			responseBody.put("message", "時段包廂已滿，需現場等候，請問是否繼續?");
		} else {
			responseBody.put("success", true);
		}
		return responseBody.toString();
	}
	
	
	
	
	

}
