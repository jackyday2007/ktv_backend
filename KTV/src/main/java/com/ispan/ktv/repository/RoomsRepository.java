package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.Rooms;



public interface RoomsRepository extends JpaRepository<Rooms, Integer> {

//	@Query("SELECT COUNT(r) FROM Rooms r WHERE " +
//	           "(CAST(:roomId AS integer) IS NULL OR r.roomId = :roomId) AND " +
//	           "(CAST(:size AS string) IS NULL OR r.size = :size) AND " +
//	           "(CAST(:price AS double) IS NULL OR r.price = :price) AND " +
//	           "(CAST(:status AS string) IS NULL OR r.status = :status)")
//	    long countRooms(@Param("roomId") Integer roomId,
//	                    @Param("size") String size,
//	                    @Param("price") Double price,
//	                    @Param("status") String status);
	
}
