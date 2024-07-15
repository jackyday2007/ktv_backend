package com.ispan.ktv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.repository.RoomsRepository;

@Service
public class RoomService {

	@Autowired
	RoomsRepository roomsRepository;

	public Rooms findRoom(Integer roomId) {
		Optional<Rooms> optional = roomsRepository.findById(roomId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

}
