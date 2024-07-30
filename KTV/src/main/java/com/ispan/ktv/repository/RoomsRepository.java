package com.ispan.ktv.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        @Query("SELECT r FROM Rooms r WHERE r.status = :status")
        public List<Rooms> findRoomsByStatus(@Param("status") String status);

        @Query("SELECT r FROM Rooms r WHERE r.size = :roomSize")
        Page<Rooms> findRoomsBySize(@Param("roomSize") String roomSize, Pageable pageable);

        // public List<Rooms> findRoomsByStatus(@Param("status") String status);

        @Query("SELECT r FROM Rooms r WHERE r.size = :size")
        public List<Rooms> findRoomsBySize(@Param("size") String size);

        @Query("FROM Rooms WHERE size = :size")
        List<Rooms> findRoomSize(@Param("size") String size);

}
