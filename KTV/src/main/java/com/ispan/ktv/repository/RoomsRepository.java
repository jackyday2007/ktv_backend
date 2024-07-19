package com.ispan.ktv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.Rooms;

public interface RoomsRepository extends JpaRepository<Rooms, Integer>, JpaSpecificationExecutor<Rooms> {

        @Query("SELECT COUNT(r) FROM Rooms r " +
                        "WHERE (:roomId IS NULL OR r.roomId = :roomId) " +
                        "AND (:size IS NULL OR r.size = :size) " +
                        "AND (:price IS NULL OR r.price = :price) " +
                        "AND (:status IS NULL OR r.status = :status)")
        long countRooms(@Param("roomId") Integer roomId,
                        @Param("size") String size,
                        @Param("price") Double price,
                        @Param("status") String status);
        
        boolean existsById(Integer roomId);
}
