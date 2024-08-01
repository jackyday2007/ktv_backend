package com.ispan.ktv.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.ktv.bean.Problems;
import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.repository.ProblemsRepository;
import com.ispan.ktv.repository.RoomsRepository;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class ProblemService {

	@Autowired
	private ProblemsRepository problemsRepository;

	@Autowired
	private RoomsRepository roomsRepository;

// 	新增
	public Problems create(String json) throws ParseException {
	    if (json == null) {
	        return null; // 或拋出例外
	    }
	    
	    JSONObject obj = new JSONObject(json);
	    String eventCase = obj.isNull("eventCase") ? null : obj.getString("eventCase");
	    Integer roomId = obj.isNull("roomId") ? null : obj.getInt("roomId");
	    String content = obj.isNull("content") ? null : obj.getString("content");
	    String eventDate = obj.isNull("eventDate") ? null : obj.getString("eventDate");
	    String closeDate = obj.isNull("closeDate") ? null : obj.getString("closeDate");
	    String status = obj.isNull("status") ? null : obj.getString("status");

	    StringBuilder errors = new StringBuilder();
	    
	    // 檢查房間是否存在
	    Optional<Rooms> roomOptional = roomsRepository.findById(roomId);
	    if (roomOptional.isEmpty()) {
	        errors.append("❌沒有包廂號碼為 ").append(roomId).append(" 的包廂。");
	    }
	    
	    // 解析日期
	    Date parsedEventDate = eventDate != null ? new SimpleDateFormat("yyyy-MM-dd").parse(eventDate) : null;
	    Date parsedCloseDate = closeDate != null ? new SimpleDateFormat("yyyy-MM-dd").parse(closeDate) : null;

	    // 檢查結束時間是否早於開始時間
	    if (parsedEventDate != null && parsedCloseDate != null && parsedCloseDate.before(parsedEventDate)) {
	        errors.append("❌結束時間不能早於開始時間。");
	    }

	    // 若有錯誤訊息，拋出例外
	    if (errors.length() > 0) {
	        throw new IllegalArgumentException(errors.toString());
	    }

	    Rooms room = roomOptional.get(); // 如果沒有錯誤，則取得房間對象

	    Problems insert = new Problems();
	    insert.setEventCase(eventCase);
	    insert.setRoom(room);
	    insert.setContent(content);
	    insert.setEventDate(parsedEventDate);
	    insert.setCloseDate(parsedCloseDate);
	    insert.setStatus(status);

	    return problemsRepository.save(insert);
	}


//	判斷
	public boolean exists(Integer problemId) {
		if (problemId != null) {
			return problemsRepository.existsById(problemId);
		}
		return false;
	}
	
//	判斷roomId	
	public boolean roomExists(Integer roomId) {
        return roomsRepository.existsById(roomId);
    }

//	 Id查詢	
	public Problems findProblemById(Integer problemId) {
		if (problemId != null) {
			Optional<Problems> optional = problemsRepository.findById(problemId);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

//	 room查詢
	public List<Problems> findProblemsByRoom(Integer room) {
	    if (room != null) {
	        return problemsRepository.findProblemsByRoom(room);
	    }
	    return null;
	}
	
//	 status查詢
	public List<Problems> findProblemsByStatus(String status) {
	    if (status != null) {
	        return problemsRepository.findProblemsByStatus(status);
	    }
	    return null;
	}

//	 修改
	public Problems modifyAndUpdateRoomStatus(String json) throws JSONException, ParseException {
	    JSONObject obj = new JSONObject(json);
	    Integer problemId = obj.isNull("problemId") ? null : obj.getInt("problemId");
	    String eventCase = obj.isNull("eventCase") ? null : obj.getString("eventCase");
	    Integer roomId = obj.isNull("roomId") ? null : obj.getInt("roomId");
	    String content = obj.isNull("content") ? null : obj.getString("content");
	    String eventDate = obj.isNull("eventDate") ? null : obj.getString("eventDate");
	    String closeDate = obj.isNull("closeDate") ? null : obj.getString("closeDate");
	    String status = obj.isNull("status") ? null : obj.getString("status");

	    Optional<Rooms> roomOptional = roomsRepository.findById(roomId);
	    if (roomOptional.isEmpty()) {
	        throw new IllegalArgumentException("Invalid roomId: " + roomId);
	    }
	    Rooms room = roomOptional.get();
	    Optional<Problems> optional = problemsRepository.findById(problemId);
	    if (optional.isPresent()) {
	        Problems update = optional.get();

	        Date createTime = update.getCreateTime();
	        update.setEventCase(eventCase);
	        update.setRoom(room);
	        update.setContent(content);
	        update.setEventDate(eventDate != null ? new SimpleDateFormat("yyyy-MM-dd").parse(eventDate) : null);
	        update.setCloseDate(closeDate != null ? new SimpleDateFormat("yyyy-MM-dd").parse(closeDate) : null);
	        update.setStatus(status);
	        update.setCreateTime(createTime);
	        update.setUpdateTime(new Date()); // 更新時間

	        Problems updatedProblem = problemsRepository.save(update);

	        // 更新房間狀態
	        if (room != null) {
	            String oldStatus = room.getStatus();
	            if ("處理中".equals(status) && !"處理中".equals(oldStatus)) {
	                room.setStatus("維護中");
	            } else if ("結案".equals(status) && !"結案".equals(oldStatus)) {
	            	room.setStatus("開放中");
	            }
	            roomsRepository.save(room);
	        }

	        return updatedProblem;
	    }
	    return null;
	}

//	找尋全部
	public List<Problems> findAll(String json, String sortField, String sortDirection) throws JSONException {
	    JSONObject body = new JSONObject(json);
	    int start = body.optInt("start", 0);
	    int max = body.optInt("max", 10);

	    // 設置排序方向
	    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
	    Sort sort = Sort.by(direction, sortField);
	    Pageable pageable = PageRequest.of(start / max, max, sort);
	    
	    return problemsRepository.findAll((root, query, criteriaBuilder) -> {
	        List<Predicate> predicate = new ArrayList<>();

	        // 查詢條件
	        if (!body.isNull("problemId")) {
	            Integer problemId = body.optInt("problemId");
	            predicate.add(criteriaBuilder.equal(root.get("problemId"), problemId));
	        }

	        if (!body.isNull("eventCase")) {
	            String eventCase = body.optString("eventCase");
	            predicate.add(criteriaBuilder.equal(root.get("eventCase"), eventCase));
	        }

	        if (!body.isNull("room")) {
	            Integer room = body.optInt("room");
	            predicate.add(criteriaBuilder.equal(root.get("room").get("roomId"), room));
	        }

	        if (!body.isNull("content")) {
	            String content = body.optString("content");
	            predicate.add(criteriaBuilder.equal(root.get("content"), content));
	        }

	        if (!body.isNull("eventDate")) {
	            String eventDateStr = body.optString("eventDate");
	            Date eventDate = java.sql.Date.valueOf(eventDateStr);
	            predicate.add(criteriaBuilder.equal(root.get("eventDate"), eventDate));
	        }

	        if (!body.isNull("closeDate")) {
	            String closeDateStr = body.optString("closeDate");
	            Date closeDate = java.sql.Date.valueOf(closeDateStr);
	            predicate.add(criteriaBuilder.equal(root.get("closeDate"), closeDate));
	        }

	        if (!body.isNull("status")) {
	            String status = body.optString("status");
	            predicate.add(criteriaBuilder.equal(root.get("status"), status));
	        }

	        if (!body.isNull("createTime")) {
	            String createTimeStr = body.optString("createTime");
	            Date createTime = java.sql.Timestamp.valueOf(createTimeStr);
	            predicate.add(criteriaBuilder.equal(root.get("createTime"), createTime));
	        }

	        if (!body.isNull("createBy")) {
	            Integer createById = body.optInt("createBy");
	            predicate.add(criteriaBuilder.equal(root.get("createBy").get("accountId"), createById));
	        }

	        if (!body.isNull("updateTime")) {
	            String updateTimeStr = body.optString("updateTime");
	            Date updateTime = java.sql.Timestamp.valueOf(updateTimeStr);
	            predicate.add(criteriaBuilder.equal(root.get("updateTime"), updateTime));
	        }

	        if (!body.isNull("updateBy")) {
	            Integer updateById = body.optInt("updateBy");
	            predicate.add(criteriaBuilder.equal(root.get("updateBy").get("accountId"), updateById));
	        }

	        query.where(predicate.toArray(new Predicate[0]));
	        return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
	    }, pageable).getContent();
	}

// 	算總筆數
	public Long count(String json) throws JSONException {
	    JSONObject body = new JSONObject(json);
	    System.out.println(body);
	    return problemsRepository.count((root, query, criteriaBuilder) -> {
	        List<Predicate> predicate = new ArrayList<>();

	        if (!body.isNull("problemId")) {
	            try {
					Integer problemId = body.getInt("problemId");
					predicate.add(criteriaBuilder.equal(root.get("problemId"), problemId));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        if (!body.isNull("eventCase")) {
	            try {
					String eventCase = body.getString("eventCase");
					predicate.add(criteriaBuilder.equal(root.get("eventCase"), eventCase));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        if (!body.isNull("room")) {
	            try {
					Integer room = body.getInt("room");
					predicate.add(criteriaBuilder.equal(root.get("room").get("roomId"), room));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        if (!body.isNull("content")) {
	            try {
					String content = body.getString("content");
					predicate.add(criteriaBuilder.equal(root.get("content"), content));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        if (!body.isNull("eventDate")) {
	            try {
					String eventDateStr = body.getString("eventDate");
					Date eventDate = java.sql.Date.valueOf(eventDateStr); // Assumes eventDateStr is in yyyy-MM-dd format
					predicate.add(criteriaBuilder.equal(root.get("eventDate"), eventDate));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        if (!body.isNull("closeDate")) {
	            try {
					String closeDateStr = body.getString("closeDate");
					Date closeDate = java.sql.Date.valueOf(closeDateStr); // Assumes closeDateStr is in yyyy-MM-dd format
					predicate.add(criteriaBuilder.equal(root.get("closeDate"), closeDate));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        if (!body.isNull("status")) {
	            try {
					String status = body.getString("status");
					predicate.add(criteriaBuilder.equal(root.get("status"), status));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        if (!body.isNull("createTime")) {
	            try {
					String createTimeStr = body.getString("createTime");
					Date createTime = java.sql.Timestamp.valueOf(createTimeStr); // Assumes createTimeStr is in yyyy-MM-dd HH:mm:ss format
					predicate.add(criteriaBuilder.equal(root.get("createTime"), createTime));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        if (!body.isNull("createBy")) {
	            try {
					Integer createById = body.getInt("createBy");
					predicate.add(criteriaBuilder.equal(root.get("createBy").get("accountId"), createById));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        if (!body.isNull("updateTime")) {
	            try {
					String updateTimeStr = body.getString("updateTime");
					Date updateTime = java.sql.Timestamp.valueOf(updateTimeStr); // Assumes updateTimeStr is in yyyy-MM-dd HH:mm:ss format
					predicate.add(criteriaBuilder.equal(root.get("updateTime"), updateTime));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        if (!body.isNull("updateBy")) {
	            try {
					Integer updateById = body.getInt("updateBy");
					predicate.add(criteriaBuilder.equal(root.get("updateBy").get("accountId"), updateById));
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }

	        query.where(predicate.toArray(new Predicate[0]));
	        return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
	    });
	}

	
	
}
