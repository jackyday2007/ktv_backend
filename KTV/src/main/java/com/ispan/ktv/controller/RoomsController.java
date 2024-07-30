package com.ispan.ktv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.service.RoomService;

@RestController
@CrossOrigin
public class RoomsController {

	@Autowired
	private RoomService roomService;

	// 新增
	@PostMapping("/rooms/create")
	public String create(@RequestBody String body) {
		JSONObject responseBody = new JSONObject();

		try {
			JSONObject obj = new JSONObject(body);
			Integer roomId = obj.isNull("roomId") ? null : obj.getInt("roomId");

			if (roomId == null) {
				responseBody.put("success", false);
				responseBody.put("message", "包廂號碼是必要欄位");
			} else {
				if (roomService.exists(roomId)) {
					responseBody.put("success", false);
					responseBody.put("message", "包廂號碼已存在❌");
				} else {
					Rooms room = roomService.create(body);
					if (room == null) {
						responseBody.put("success", false);
						responseBody.put("message", "新增失敗");
					} else {
						responseBody.put("success", true);
						responseBody.put("message", "新增成功✔");
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseBody.toString();
	}

	// RoomId單筆查詢
	@GetMapping("/rooms/findByRoomId/{pk}")
	public String findByRoomId(@PathVariable(name = "pk") Integer roomId) {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Rooms room = roomService.findByRoomId(roomId);
			if (room != null) {
				JSONObject item = new JSONObject().put("roomId", room.getRoomId()).put("size", room.getSize())
						.put("price", room.getPrice()).put("status", room.getStatus())
						.put("photoFile", room.getPhotoFile()).put("createTime", room.getCreateTime())
						.put("createBy", room.getCreateBy()).put("updateTime", room.getUpdateTime())
						.put("updateBy", room.getUpdateBy());
				array.put(item);
			}
			responseBody.put("list", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseBody.toString();
	}

	// status查詢
	@GetMapping("/rooms/findByRoomStatus/{status}")
	public String findByRoomStatus(@PathVariable(name = "status") String status) throws JSONException {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		List<Rooms> rooms = roomService.findRoomsByStatus(status);
		if (rooms != null) {
			for (Rooms room : rooms) {
				JSONObject item = new JSONObject()
						.put("roomId", room.getRoomId())
						.put("size", room.getSize())
						.put("price", room.getPrice())
						.put("status", room.getStatus())
						.put("photoFile", room.getPhotoFile())
						.put("createTime", room.getCreateTime())
						.put("createBy", room.getCreateBy())
						.put("updateTime", room.getUpdateTime())
						.put("updateBy", room.getUpdateBy());
				array.put(item);
			}
		}
		responseBody.put("list", array);
		return responseBody.toString();
	}
	
	
	// size查詢
	@GetMapping("/rooms/findByRoomSize/{roomSize}")
	public String findByRoomSize(@PathVariable(name = "roomSize") String roomSize,
	                             @RequestParam(defaultValue = "0") int pageNumber,
	                             @RequestParam(defaultValue = "10") int pageSize) throws JSONException {
	    JSONObject responseBody = new JSONObject();
	    JSONArray array = new JSONArray();

	    Page<Rooms> roomPage = roomService.findRoomsBySize(roomSize, pageNumber, pageSize);
	    for (Rooms room : roomPage.getContent()) {
	        JSONObject item = new JSONObject()
	                .put("roomId", room.getRoomId())
	                .put("size", room.getSize())
	                .put("price", room.getPrice())
	                .put("status", room.getStatus())
	                .put("photoFile", room.getPhotoFile())
	                .put("createTime", room.getCreateTime())
	                .put("createBy", room.getCreateBy())
	                .put("updateTime", room.getUpdateTime())
	                .put("updateBy", room.getUpdateBy());
	        array.put(item);
	    }
	    responseBody.put("list", array);
	    responseBody.put("totalPages", roomPage.getTotalPages());
	    responseBody.put("totalElements", roomPage.getTotalElements());
	    responseBody.put("currentPage", roomPage.getNumber());
	    responseBody.put("pageSize", roomPage.getSize());
	    return responseBody.toString();
	}

	// 修改資料
	@PutMapping("/rooms/modify/{roomId}")
	public String modify(@PathVariable Integer roomId, @RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		try {
			if (roomId == null) {
				responseBody.put("success", false);
				responseBody.put("message", "roomId是必要欄位");
			} else if (!roomService.exists(roomId)) {
				responseBody.put("success", false);
				responseBody.put("message", "包廂號碼固定，無法修改❌");
			} else {
				boolean hasProblem = roomService.checkRoomProblems(roomId);
				JSONObject obj = new JSONObject(body);
				String newStatus = obj.getString("status");

				if (hasProblem && !"處理中".equals(newStatus)) {
					responseBody.put("success", false);
					responseBody.put("message", "包廂有問題且狀態為處理中，無法修改❌");
				} else {
					// 更新包廂信息
					Rooms updatedRoom = roomService.modify(body);

					// 根據問題狀態更新包廂狀態
					roomService.updateRoomStatus(roomId, newStatus);

					if (updatedRoom == null) {
						responseBody.put("success", false);
						responseBody.put("message", "修改失敗");
					} else {
						responseBody.put("success", true);
						responseBody.put("message", "修改成功✔");
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseBody.toString();
	}

	// 查詢全部
	@PostMapping("/rooms/findAll")
	public String findAll(@RequestBody String body) throws JSONException {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		List<Rooms> rooms = roomService.findAll(body);
		if (rooms != null && !rooms.isEmpty()) {
			for (Rooms room : rooms) {
				JSONObject item = new JSONObject().put("roomId", room.getRoomId()).put("size", room.getSize())
						.put("price", room.getPrice()).put("status", room.getStatus())
						.put("photoFile", room.getPhotoFile()).put("createTime", room.getCreateTime())
						.put("createBy", room.getCreateBy()).put("updateTime", room.getUpdateTime())
						.put("updateBy", room.getUpdateBy());
				array.put(item);
			}
			long count = roomService.count(body);
			responseBody.put("count", count);
			responseBody.put("list", array);
			return responseBody.toString();
		}
		return responseBody.toString();
	}

}
