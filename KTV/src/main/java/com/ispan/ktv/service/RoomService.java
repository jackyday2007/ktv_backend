package com.ispan.ktv.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.repository.RoomsRepository;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class RoomService {

	@Autowired
	private RoomsRepository roomsRepository;

	// 新增
	public Rooms create(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			Integer roomId = obj.isNull("roomId") ? null : obj.getInt("roomId");
			String size = obj.isNull("size") ? null : obj.getString("size");
			Double price = obj.isNull("price") ? null : obj.getDouble("price");
			String status = obj.isNull("status") ? null : obj.getString("status");
			// String photoFile = obj.isNull("photoFile") ? null :
			// obj.getString("photoFile");

			Optional<Rooms> optional = roomsRepository.findById(roomId);
			if (optional.isEmpty()) {
				Rooms insert = new Rooms();
				insert.setRoomId(roomId);
				insert.setSize(size);
				insert.setPrice(price);
				insert.setStatus(status);
				// room.setPhotoFile(photoFile);

				return roomsRepository.save(insert);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 判斷
	public boolean exists(Integer roomId) {
		if (roomId != null) {
			return roomsRepository.existsById(roomId);
		}
		return false;
	}

	// 修改
	public Rooms modify(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			Integer roomId = obj.isNull("roomId") ? null : obj.getInt("roomId");
			String size = obj.isNull("size") ? null : obj.getString("size");
			Double price = obj.isNull("price") ? null : obj.getDouble("price");
			String status = obj.isNull("status") ? null : obj.getString("status");
			// String photoFile = obj.isNull("photoFile") ? null :
			// obj.getString("photoFile");

			Optional<Rooms> optional = roomsRepository.findById(roomId);
			if (optional.isPresent()) {
				Rooms update = optional.get();
				Date createTime = update.getCreateTime();
				update.setRoomId(roomId);
				update.setSize(size);
				update.setPrice(price);
				update.setStatus(status);
				// room.setPhotoFile(photoFile);
				update.setCreateTime(createTime);
				update.setUpdateTime(new Date());

				return roomsRepository.save(update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Id查詢
	public Rooms findByRoomId(Integer roomId) {
		if (roomId != null) {
			Optional<Rooms> optional = roomsRepository.findById(roomId);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	// 找尋全部
	public List<Rooms> findAll(String json) throws JSONException {
	    JSONObject body = new JSONObject(json);
	    int start = body.optInt("start", 0);
	    int max = body.optInt("max", 10);

	    Pageable pageable = PageRequest.of(start / max, max);

	    return roomsRepository.findAll((root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (!body.isNull("roomId")) {
	            Integer roomId = body.optInt("roomId");
	            predicates.add(criteriaBuilder.equal(root.get("roomId"), roomId));
	        }

	        if (!body.isNull("size")) {
	            String size = body.optString("size");
	            predicates.add(criteriaBuilder.equal(root.get("size"), size));
	        }

	        if (!body.isNull("price")) {
	            Double price = body.optDouble("price");
	            predicates.add(criteriaBuilder.equal(root.get("price"), price));
	        }

	        if (!body.isNull("status")) {
	            String status = body.optString("status");
	            predicates.add(criteriaBuilder.equal(root.get("status"), status));
	        }

	        if (!body.isNull("createTime")) {
	            String createTimeStr = body.optString("createTime");
	            Date createTime = java.sql.Timestamp.valueOf(createTimeStr);
	            predicates.add(criteriaBuilder.equal(root.get("createTime"), createTime));
	        }

	        if (!body.isNull("createBy")) {
	            String createBy = body.optString("createBy");
	            predicates.add(criteriaBuilder.equal(root.get("createBy"), createBy));
	        }

	        if (!body.isNull("updateTime")) {
	            String updateTimeStr = body.optString("updateTime");
	            Date updateTime = java.sql.Timestamp.valueOf(updateTimeStr);
	            predicates.add(criteriaBuilder.equal(root.get("updateTime"), updateTime));
	        }

	        if (!body.isNull("updateBy")) {
	            String updateBy = body.optString("updateBy");
	            predicates.add(criteriaBuilder.equal(root.get("updateBy"), updateBy));
	        }

	        query.where(predicates.toArray(new Predicate[0]));
	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    }, pageable).getContent();
	}

//	public List<Rooms> findAll(String json) {
//		try {
//			// JSONObject obj = new JSONObject(json);
//			return roomsRepository.findAll();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	// 總筆數
	public Long count(String json) throws JSONException {
	    JSONObject body = new JSONObject(json);

	    return roomsRepository.count((root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (!body.isNull("roomId")) {
	            Integer roomId = body.optInt("roomId");
	            predicates.add(criteriaBuilder.equal(root.get("roomId"), roomId));
	        }

	        if (!body.isNull("size")) {
	            String size = body.optString("size");
	            predicates.add(criteriaBuilder.equal(root.get("size"), size));
	        }

	        if (!body.isNull("price")) {
	            Double price = body.optDouble("price");
	            predicates.add(criteriaBuilder.equal(root.get("price"), price));
	        }

	        if (!body.isNull("status")) {
	            String status = body.optString("status");
	            predicates.add(criteriaBuilder.equal(root.get("status"), status));
	        }

	        if (!body.isNull("createTime")) {
	            String createTimeStr = body.optString("createTime");
	            Date createTime = java.sql.Timestamp.valueOf(createTimeStr);
	            predicates.add(criteriaBuilder.equal(root.get("createTime"), createTime));
	        }

	        if (!body.isNull("createBy")) {
	            String createBy = body.optString("createBy");
	            predicates.add(criteriaBuilder.equal(root.get("createBy"), createBy));
	        }

	        if (!body.isNull("updateTime")) {
	            String updateTimeStr = body.optString("updateTime");
	            Date updateTime = java.sql.Timestamp.valueOf(updateTimeStr);
	            predicates.add(criteriaBuilder.equal(root.get("updateTime"), updateTime));
	        }

	        if (!body.isNull("updateBy")) {
	            String updateBy = body.optString("updateBy");
	            predicates.add(criteriaBuilder.equal(root.get("updateBy"), updateBy));
	        }

	        query.where(predicates.toArray(new Predicate[0]));
	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    });
	}
//	public long count(String json) {
//		try {
//			JSONObject obj = new JSONObject(json);
//			Integer roomId = obj.isNull("roomId") ? null : obj.getInt("roomId");
//			String size = obj.isNull("size") ? null : obj.getString("size");
//			Double price = obj.isNull("price") ? null : obj.getDouble("price");
//			String status = obj.isNull("status") ? null : obj.getString("status");
//
//			return roomsRepository.countRooms(roomId, size, price, status);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

	public void deleteRoomsById(Integer roomId) {
		roomsRepository.deleteById(roomId);
	}

}
