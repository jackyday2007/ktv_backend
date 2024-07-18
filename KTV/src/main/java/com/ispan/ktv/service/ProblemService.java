package com.ispan.ktv.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.ktv.bean.Problems;
import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.repository.ProblemsRepository;
import com.ispan.ktv.repository.RoomsRepository;

@Service
@Transactional
public class ProblemService {

	@Autowired
	private ProblemsRepository problemsRepository;

	@Autowired
	private RoomsRepository roomsRepository;

// 	新增
	public Problems create(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String eventCase = obj.isNull("eventCase") ? null : obj.getString("eventCase");
			Integer roomId = obj.isNull("room") ? null : obj.getInt("room");
			String content = obj.isNull("content") ? null : obj.getString("content");
			String eventDate = obj.isNull("eventDate") ? null : obj.getString("eventDate");
			String closeDate = obj.isNull("closeDate") ? null : obj.getString("closeDate");
			String status = obj.isNull("status") ? null : obj.getString("status");

			Optional<Rooms> roomOptional = roomsRepository.findById(roomId);
			if (roomOptional.isEmpty()) {
				// 如果 roomId 無效，可以選擇拋出例外或傳回 null
				throw new IllegalArgumentException("沒有" + roomId);
			}
			Rooms room = roomOptional.get();

			Problems insert = new Problems();
			insert.setEventCase(eventCase);
			insert.setRoom(room);
			insert.setContent(content);
			// 看有無需要 (時分秒)
			insert.setEventDate(eventDate != null ? new SimpleDateFormat("yyyy-MM-dd").parse(eventDate) : null);
			insert.setCloseDate(closeDate != null ? new SimpleDateFormat("yyyy-MM-dd").parse(closeDate) : null);
			insert.setStatus(status);

			return problemsRepository.save(insert);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

//	判斷
	public boolean exists(Integer problemId) {
		if (problemId != null) {
			return problemsRepository.existsById(problemId);
		}
		return false;
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
	public Problems modify(String json) throws JSONException, ParseException {

		JSONObject obj = new JSONObject(json);
		Integer problemId = obj.isNull("problemId") ? null : obj.getInt("problemId");
		String eventCase = obj.isNull("eventCase") ? null : obj.getString("eventCase");
		Integer roomId = obj.isNull("room") ? null : obj.getInt("room");
		String content = obj.isNull("content") ? null : obj.getString("content");
		String eventDate = obj.isNull("eventDate") ? null : obj.getString("eventDate");
		String closeDate = obj.isNull("closeDate") ? null : obj.getString("closeDate");
		String status = obj.isNull("status") ? null : obj.getString("status");

		Optional<Rooms> roomOptional = roomsRepository.findById(roomId);
		if (roomOptional.isEmpty()) {
			// 如果 roomId 無效，可以選擇拋出例外或傳回 null
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
			// 看有無需要 (時分秒)
			update.setEventDate(
					eventDate != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eventDate) : null);
			update.setCloseDate(
					closeDate != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(closeDate) : null);
			update.setStatus(status);
			update.setCreateTime(createTime);
			update.setUpdateTime(new Date());

			return problemsRepository.save(update);
		}
		return null;
	}

//	找尋全部
	public List<Problems> findAll(String json) {
		return problemsRepository.findAll();
	}

//	總筆數
	public long count(String json) throws JSONException {
		JSONObject obj = new JSONObject(json);
		Integer problemId = obj.isNull("problemId") ? null : obj.getInt("problemId");

		return problemsRepository.countProblems(problemId);
	}

	public void deleteProblemById(Integer problemId) {
		problemsRepository.deleteById(problemId);
	}

}
