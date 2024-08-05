package com.ispan.ktv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Photos;
import com.ispan.ktv.repository.PhotosRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PhotoService {

	@Autowired
	private PhotosRepository photosRepo;

	public Photos insertPhoto(Photos photos) {
		return photosRepo.save(photos);
	}

	public Photos findById(Integer id) {
		Optional<Photos> optional = photosRepo.findById(id);
		if (optional.isEmpty()) {
			return null;
		}
		return optional.get();
	}

	public List<Photos> findAll() {
		return photosRepo.findAll();
	}

	public void delete(Integer id) {
		// photosRepo.findById(id);
		// if (id != null) {
		photosRepo.deleteById(id);
		// }
	}

	public List<Photos> insertPhotos(List<Photos> photosList) {
		return photosRepo.saveAll(photosList);
	}
}
