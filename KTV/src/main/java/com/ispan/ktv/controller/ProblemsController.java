package com.ispan.ktv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Problems;
import com.ispan.ktv.service.ProblemService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProblemsController {

	@Autowired
	private ProblemService problemService;

	@PostMapping("/problems/create")
	public String create(@RequestBody String body) {
		JSONObject responseBody = new JSONObject();

		try {
			JSONObject obj = new JSONObject(body);
			Integer problemId = obj.isNull("problemId") ? null : obj.getInt("problemId");
			if (problemId == null) {
				responseBody.put("success", false);
				responseBody.put("message", "problemId是必要欄位");
			} else {
				if (problemService.exists(problemId)) {
					responseBody.put("success", false);
					responseBody.put("message", "problemId已存在");
				} else {
					Problems problem = problemService.create(body);
					if (problem == null) {
						responseBody.put("success", false);
						responseBody.put("message", "新增失敗");
					} else {
						responseBody.put("success", true);
						responseBody.put("message", "新增成功");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseBody.toString();
	}
	
	// RoomId單筆查詢
	@GetMapping("/problems/findByProblemId/{pk}")
	public String findById(@PathVariable(name = "pk") Integer problemId) throws JSONException {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		Problems problem = problemService.findProblemById(problemId);
		if(problemId != null) {
			JSONObject item = new JSONObject()
//					.put("problemId",problem.getProblemId())
					.put("eventCase",problem.getEventCase())
					.put("room",problem.getRoom())
					.put("content",problem.getContent())
					.put("eventDate",problem.getEventDate())
					.put("closeDate",problem.getCloseDate())
					.put("status",problem.getStatus());
			array.put(item);
		}
		responseBody.put("list", array);
		
		return responseBody.toString();
	}
	
}
