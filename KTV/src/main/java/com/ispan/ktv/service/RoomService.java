package com.ispan.ktv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.repository.RoomsRepository;

@Service
public class RoomService {
	
	@Autowired
	private RoomsRepository roomsRepository;
	
	public Rooms insertOrUpdateRooms(Rooms room) {
		return roomsRepository.save(room);
	}
	
	public Rooms findRoomById(Integer roomId) {
		Optional<Rooms> optional = roomsRepository.findById(roomId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<Rooms> findAllRoom(){
		return roomsRepository.findAll();
	}

	public void deleteRoomsById(Integer roomId) {
		roomsRepository.deleteById(roomId);
	}
	
}
