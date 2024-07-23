package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ispan.ktv.bean.Photos;

public interface PhotosRepository extends JpaRepository<Photos, Integer> {

}
