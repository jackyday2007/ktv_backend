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

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ProblemsController {

	@Autowired
	private ProblemService problemService;

	// 新增
	@PostMapping("/problems/create")
	public String create(@RequestBody String body) throws JSONException {
		JSONObject responseBody = new JSONObject();
		try {
			Problems problem = problemService.create(body);
			if (problem == null) {
				responseBody.put("success", false);
				responseBody.put("message", "包廂問題新增失敗");
			} else {
				responseBody.put("success", true);
				responseBody.put("message", "包廂問題新增成功");
			}
		} catch (IllegalArgumentException e) {
			responseBody.put("success", false);
			responseBody.put("message", "新增失敗!" + e.getMessage() + "包廂號碼");
		}
		return responseBody.toString();
	}

	// ProblemId單筆查詢
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
					.put("closeDate", problem.getCloseDate()).put("status", problem.getStatus())
					.put("createTime", problem.getCreateTime()).put("createBy", problem.getCreateBy())
					.put("updateTime", problem.getUpdateTime()).put("updateBy", problem.getUpdateBy());
			array.put(item);
		}
		responseBody.put("list", array);
		return responseBody.toString();
	}

	// ProblemRoom單筆查詢
	@GetMapping("/problems/findProblemsByRoom/{room}")
	public String findProblemsByRoom(@PathVariable(name = "room") Integer room) throws JSONException {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();

		List<Problems> problems = problemService.findProblemsByRoom(room);
		if (problems != null) {
			for (Problems problem : problems) {
				Rooms problemRoom = problem.getRoom();
				JSONObject item = new JSONObject().put("problemId", problem.getProblemId())
						.put("eventCase", problem.getEventCase()).put("roomId", problemRoom.getRoomId())
						.put("content", problem.getContent()).put("eventDate", problem.getEventDate())
						.put("closeDate", problem.getCloseDate()).put("status", problem.getStatus())
						.put("createTime", problem.getCreateTime()).put("createBy", problem.getCreateBy())
						.put("updateTime", problem.getUpdateTime()).put("updateBy", problem.getUpdateBy());
				array.put(item);
			}
		}

		responseBody.put("list", array);
		return responseBody.toString();
	}

	// ProblemStatus單筆查詢
	@GetMapping("/problems/findProblemsByStatus/{status}")
	public String findProblemsByRoom(@PathVariable(name = "status") String status) throws JSONException {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();

		List<Problems> problems = problemService.findProblemsByStatus(status);
		if (problems != null) {
			for (Problems problem : problems) {
				Rooms problemRoom = problem.getRoom();
				JSONObject item = new JSONObject().put("problemId", problem.getProblemId())
						.put("eventCase", problem.getEventCase()).put("roomId", problemRoom.getRoomId())
						.put("content", problem.getContent()).put("eventDate", problem.getEventDate())
						.put("closeDate", problem.getCloseDate()).put("status", problem.getStatus())
						.put("createTime", problem.getCreateTime()).put("createBy", problem.getCreateBy())
						.put("updateTime", problem.getUpdateTime()).put("updateBy", problem.getUpdateBy());
				array.put(item);
			}
		}
		responseBody.put("list", array);
		return responseBody.toString();
	}

	// 修改資料
	@PutMapping("/problems/modify/{problemId}")
	public String modify(@PathVariable Integer problemId, @RequestBody String body)
			throws JSONException, ParseException {
		JSONObject responseBody = new JSONObject();
		JSONObject obj = new JSONObject(body);
		Integer bodyProblemId = obj.isNull("problemId") ? null : obj.getInt("problemId");

		// 檢查路徑中的 problemId 和請求體中的 problemId 是否一致
		if (problemId == null || !problemId.equals(bodyProblemId)) {
			responseBody.put("success", false);
			responseBody.put("message", "包廂號碼不一致");
		} else {
			if (!problemService.exists(problemId)) {
				responseBody.put("success", false);
				responseBody.put("message", "包廂號碼不存在");
			} else {
				Problems problem = problemService.modify(body);
				if (problem == null) {
					responseBody.put("success", false);
					responseBody.put("message", "包廂問題修改失敗");
				} else {
					responseBody.put("success", true);
					responseBody.put("message", "包廂問題修改成功");
				}
			}
		}
		return responseBody.toString();
	}

	// 查詢全部
	@PostMapping("/problems/findAll")
	public String findAll(@RequestBody String body) throws JSONException {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		// 檢查 body 是否為空，為空時設置為默認值或處理為空查詢
		if (body == null || body.isEmpty()) {
			body = "{}"; // 或者設置為其他合理的默認值
		}
		List<Problems> problems = problemService.findAll(body);
		if (problems != null && !problems.isEmpty()) {
			for (Problems problem : problems) {
				Integer roomId = null;
				if (problem.getRoom() != null) {
					roomId = problem.getRoom().getRoomId();
				}
				JSONObject item = new JSONObject().put("problemId", problem.getProblemId())
						.put("eventCase", problem.getEventCase()).put("room", roomId) // 將 roomId 放入
						.put("content", problem.getContent()).put("eventDate", problem.getEventDate())
						.put("closeDate", problem.getCloseDate()).put("status", problem.getStatus())
						.put("createTime", problem.getCreateTime()).put("createBy", problem.getCreateBy())
						.put("updateTime", problem.getUpdateTime()).put("updateBy", problem.getUpdateBy());
				array.put(item);
			}
			long count = problemService.count(body);
			responseBody.put("count", count);
			responseBody.put("list", array);
			return responseBody.toString();
		}
		responseBody.put("list", array); // 即使 problems 為空，也返回空的 list
		return responseBody.toString();
	}

}
