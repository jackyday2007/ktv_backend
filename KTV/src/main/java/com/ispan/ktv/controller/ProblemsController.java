package com.ispan.ktv.controller;

import java.text.ParseException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Problems;
import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.service.ProblemService;
import com.ispan.ktv.service.RoomService;

@RequestMapping("/ktvbackend/")
@RestController
@CrossOrigin
public class ProblemsController {

	@Autowired
	private ProblemService problemService;

	@Autowired
	private RoomService roomService;

	// 新增
	@PostMapping("/problems/create")
	public String create(@RequestBody String body) throws ParseException {
		JSONObject responseBody = new JSONObject();
		try {
			Problems problem = problemService.create(body);
			if (problem == null) {
				responseBody.put("success", false);
				responseBody.put("message", "包廂問題新增失敗");
			} else {
				responseBody.put("success", true);
				responseBody.put("message", "包廂問題新增成功✔");
			}
		} catch (IllegalArgumentException e) {
			responseBody.put("success", false);
			responseBody.put("message",e.getMessage());
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
				JSONObject item = new JSONObject()
						.put("problemId", problem.getProblemId())
						.put("eventCase", problem.getEventCase())
						.put("roomId", problemRoom.getRoomId())
						.put("content", problem.getContent())
						.put("eventDate", problem.getEventDate())
						.put("closeDate", problem.getCloseDate())
						.put("status", problem.getStatus())
						.put("createTime", problem.getCreateTime())
						.put("createBy", problem.getCreateBy())
						.put("updateTime", problem.getUpdateTime())
						.put("updateBy", problem.getUpdateBy());
				array.put(item);
			}
		}
		responseBody.put("list", array);
		return responseBody.toString();
	}

	// 修改資料
	@PutMapping("/problems/modify/{problemId}")
	public String modify(@PathVariable Integer problemId, @RequestBody String body) throws JSONException, ParseException {
	    JSONObject responseBody = new JSONObject();
	        JSONObject obj = new JSONObject(body);
	        Integer bodyProblemId = obj.isNull("problemId") ? null : obj.getInt("problemId");
	        Integer roomId = obj.isNull("roomId") ? null : obj.getInt("roomId");

	        // 檢查路徑中的 problemId 和請求體中的 problemId 是否一致
	        if (problemId == null || !problemId.equals(bodyProblemId)) {
	            responseBody.put("success", false);
	            responseBody.put("message", "問題編號不一致");
	        } else {
	            if (!problemService.exists(problemId)) {
	                responseBody.put("success", false);
	                responseBody.put("message", "問題編號不存在");
	            } else {
	                // 檢查 roomId 是否存在
	                if (roomId != null && !roomService.exists(roomId)) {
	                    responseBody.put("success", false);
	                    responseBody.put("message", "包廂編號不存在");
	                } else {
	                    // 修改問題並更新房間狀態
	                    Problems problem = problemService.modifyAndUpdateRoomStatus(body);
	                    if (problem == null) {
	                        responseBody.put("success", false);
	                        responseBody.put("message", "包廂問題修改失敗");
	                    } else {
	                        responseBody.put("success", true);
	                        responseBody.put("message", "包廂問題修改成功");
	                    }
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

	    // 設置默認的排序方向為升序
	    String sortDirection = "asc";
	    String sortField = "eventDate"; // 默認排序字段為事件時間

	    // 將 JSON 字串轉為 JSONObject 以便讀取排序參數
	    JSONObject requestObject = new JSONObject(body);
	    if (requestObject.has("sortDirection")) {
	        sortDirection = requestObject.getString("sortDirection");
	    }
	    if (requestObject.has("sortField")) {
	        sortField = requestObject.getString("sortField");
	    }

	    // 檢查 body 是否為空，為空時設置為默認值或處理為空查詢
	    if (body == null || body.isEmpty()) {
	        body = "{}"; // 或者設置為其他合理的默認值
	    }

	    List<Problems> problems = problemService.findAll(body, sortField, sortDirection);
	    if (problems != null && !problems.isEmpty()) {
	        for (Problems problem : problems) {
	            Integer roomId = null;
	            if (problem.getRoom() != null) {
	                roomId = problem.getRoom().getRoomId();
	            }
	            JSONObject item = new JSONObject()
	                    .put("problemId", problem.getProblemId())
	                    .put("eventCase", problem.getEventCase())
	                    .put("room", roomId) // 將 roomId 放入
	                    .put("content", problem.getContent())
	                    .put("eventDate", problem.getEventDate())
	                    .put("closeDate", problem.getCloseDate())
	                    .put("status", problem.getStatus())
	                    .put("createTime", problem.getCreateTime())
	                    .put("createBy", problem.getCreateBy())
	                    .put("updateTime", problem.getUpdateTime())
	                    .put("updateBy", problem.getUpdateBy());
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
