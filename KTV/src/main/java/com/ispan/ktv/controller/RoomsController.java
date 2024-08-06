package com.ispan.ktv.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.RoomHistory;
import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.service.RoomService;

@RequestMapping("/ktvbackend/")
@RestController
@CrossOrigin
public class RoomsController {

	@Autowired
	private RoomService roomService;

	// 查詢時間範圍內的 RoomHistory
	@GetMapping("/roomHistory/findByTimeRange")
	public String findRoomHistoryByTimeRange(
	        @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	        @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws JSONException {

	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	    JSONObject responseBody = new JSONObject();
	    JSONArray array = new JSONArray();

	    List<RoomHistory> histories = roomService.findRoomHistoryByTimeRange(startDate, endDate);
	    for (RoomHistory history : histories) {
	        // 使用 Stream API 來將 List<Long> 轉換為 List<String>
	        List<String> orderIds = history.getRoom().getRoomOrders().stream()
	                .map(order -> String.valueOf(order.getOrderId())) // 轉換 Long 為 String
	                .collect(Collectors.toList());

	        // 確保 orderIds 不為空，並取最後一個 orderId
	        String lastOrderId = orderIds.isEmpty() ? null : orderIds.get(orderIds.size() - 1);

	        JSONObject item = new JSONObject()
	                .put("id", history.getId())
	                .put("orderId", lastOrderId) // 使用最後一個 orderId
	                .put("roomId", history.getRoom().getRoomId())
	                .put("size", history.getRoom().getSize())
	                .put("date", dateFormat.format(history.getDate()))
	                .put("startTime", timeFormat.format(history.getStartTime()))
	                .put("endTime", timeFormat.format(history.getEndTime()))
	                .put("status", history.getStatus())
	                .put("createTime", timeFormat.format(history.getCreateTime()));
	        array.put(item);
	    }
	    responseBody.put("list", array);
	    return responseBody.toString();
	}


	// 新增
	@PostMapping("/rooms/create")
	public String create(@RequestBody String body) {
		JSONObject responseBody = new JSONObject();

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
		return responseBody.toString();
	}

	// RoomId單筆查詢
	@GetMapping("/rooms/findByRoomId/{pk}")
	public String findByRoomId(@PathVariable(name = "pk") Integer roomId) {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();

		Rooms room = roomService.findByRoomId(roomId);
		if (room != null) {
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

		return responseBody.toString();
	}

	@GetMapping("/rooms/checkStatus/{roomId}")
	public String checkStatus(@PathVariable(name = "roomId") Integer roomId) {
		JSONObject responseBody = new JSONObject();
		Rooms room = roomService.findByRoomId(roomId);
		if (room.getStatus().equals("維護中")) {
			responseBody.put("success", false);
			responseBody.put("message", "包廂維護中，請派發其他包廂");
		} else {
			responseBody.put("success", true);
			responseBody.put("message", "可使用");
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

		if (roomId == null) {
			responseBody.put("success", false);
			responseBody.put("message", "roomId是必要欄位");
		} else if (!roomService.exists(roomId)) {
			responseBody.put("success", false);
			responseBody.put("message", "包廂號碼固定，無法修改⚠");
		} else {
			boolean hasProblem = roomService.checkRoomProblems(roomId); // 確認包廂是否有問題
			boolean isUsing = roomService.checkRoomUsing(roomId); // 確認包廂是否在使用中
			JSONObject obj = new JSONObject(body);
			String newStatus = obj.getString("status");

			// 先檢查是否在使用中
			if (isUsing) {
				responseBody.put("success", false);
				responseBody.put("message", "包廂正在使用中，無法修改⚠");
			} else if (hasProblem && !"處理中".equals(newStatus)) {
				// 如果有問題且新狀態不是「處理中」，返回相應錯誤
				responseBody.put("success", false);
				responseBody.put("message", "包廂有問題處理中，無法修改⚠");
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

		return responseBody.toString();
	}

	// 查詢全部(無)分頁
	@PostMapping("/rooms/findAllNoPage")
	public String findAllNoPage(@RequestBody String body) throws JSONException {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		List<Rooms> rooms = roomService.findAllNoPage(body); // 獲取所有資料
		if (rooms != null && !rooms.isEmpty()) {
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
			long count = roomService.count(body);
			responseBody.put("count", count);
			responseBody.put("list", array);
			return responseBody.toString();
		}
		return responseBody.toString();
	}

	// 查詢全部有分頁
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

	@DeleteMapping("/rooms/delete/{roomId}")
	public String delete(@PathVariable(name = "roomId") Integer roomId) {
		roomService.findByRoomId(roomId);
		if (roomId != null) {
			return "查無ID~~";
		}
		roomService.deleteRoomsById(roomId);
		return "刪除成功";
	}
}
