package com.ispan.ktv.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Problems;
import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.service.ProblemService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProblemsController {

	@Autowired
	private ProblemService problemService;

//	新增
	@PostMapping("/problems/create")
	public String create(@RequestBody String body) throws JSONException {
		JSONObject responseBody = new JSONObject();
		Problems problem = problemService.create(body);
		if (problem == null) {
			responseBody.put("success", false);
			responseBody.put("message", "新增失敗");
		} else {
			responseBody.put("success", true);
			responseBody.put("message", "新增成功");
		}
		return responseBody.toString();
	}

//	RoomId單筆查詢
	@GetMapping("/problems/findByProblemId/{pk}")
	public String findById(@PathVariable(name = "pk") Integer problemId) throws JSONException {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		Problems problem = problemService.findProblemById(problemId);
		if (problemId != null) {
			Rooms room = problem.getRoom();
			JSONObject item = new JSONObject().put("problemId", problem.getProblemId())
					.put("eventCase", problem.getEventCase()).put("roomId", room.getRoomId())
					.put("content", problem.getContent()).put("eventDate", problem.getEventDate())
					.put("closeDate", problem.getCloseDate()).put("status", problem.getStatus());

			array.put(item);
		}
		responseBody.put("list", array);
		return responseBody.toString();
	}

// 	修改資料
	@PutMapping("/problems/modify/{problemId}")
	public String modify(@PathVariable Integer problemId, @RequestBody String body)
			throws JSONException, ParseException {
		JSONObject responseBody = new JSONObject();
		JSONObject obj = new JSONObject(body);
		Integer bodyProblemId = obj.isNull("problemId") ? null : obj.getInt("problemId");

		// 檢查路徑中的 problemId 和請求體中的 problemId 是否一致
		if (problemId == null || !problemId.equals(bodyProblemId)) {
			responseBody.put("success", false);
			responseBody.put("message", "problemId 不一致");
		} else {
			if (!problemService.exists(problemId)) {
				responseBody.put("success", false);
				responseBody.put("message", "problemId 不存在");
			} else {
				Problems problem = problemService.modify(body);
				if (problem == null) {
					responseBody.put("success", false);
					responseBody.put("message", "修改失敗");
				} else {
					responseBody.put("success", true);
					responseBody.put("message", "修改成功");
				}
			}
		}
		return responseBody.toString();
	}

// 	查詢全部
	@PostMapping("/problems/findAll")
	public String findAll(@RequestBody String body) throws JSONException {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		List<Problems> problems = problemService.findAll(body);
		if(problems != null && !problems.isEmpty()) {
			for(Problems problem : problems) {
				JSONObject item = new JSONObject()
						.put("problemId", problem.getProblemId())
						.put("eventCase", problem.getEventCase())
						.put("room", problem.getRoom())
						.put("content", problem.getContent())
						.put("eventDate", problem.getEventDate())
						.put("closeDate", problem.getCloseDate())
						.put("status", problem.getStatus());
				array.put(item);
			}
			long count = problemService.count(body);
			responseBody.put("count",count);
			responseBody.put("list", array);
			return responseBody.toString();
		}
		return responseBody.toString();
	}
}
