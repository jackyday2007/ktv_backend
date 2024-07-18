package com.ispan.ktv.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.TimeSlot;
import com.ispan.ktv.service.TimeSlotService;

@RestController
public class TimeSlotController {

	@Autowired
	private TimeSlotService ts;

	@GetMapping("/find/time")
	public List<TimeSlot> putMethodName(@RequestParam String wst, @RequestParam String wet) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date wantedStartTime = formatter.parse(wst);
		Date wantedEndTime = formatter.parse(wet);

		if (wst == null) {
			wantedStartTime = new Date();
			return ts.findByTime(wantedStartTime, wantedEndTime);
		}
		if (wet == null) {
			wantedEndTime = formatter.parse("2099-12-12");

			return ts.findByTime(wantedStartTime, wantedEndTime);
		}
		System.out.println(wantedStartTime);
		return ts.findByTime(wantedStartTime, wantedEndTime);
	}

	@PostMapping("/timeSlot/create")
	public String create(@RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		try {
			TimeSlot product = ts.create(body);
			if (product == null) {
				responseBody.put("success", false);
				responseBody.put("message", "新增失敗");
			} else {
				responseBody.put("success", true);
				responseBody.put("message", "新增成功");
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return responseBody.toString();
	}

}
