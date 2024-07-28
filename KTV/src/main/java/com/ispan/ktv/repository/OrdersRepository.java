package com.ispan.ktv.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.ktv.bean.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {

   long countByCreateTime(Date CreateTime);
   
   // test1
   
   // 有status條件搜尋
   @Query("SELECT o FROM Orders o LEFT JOIN o.ordersStatusHistory osh " +
           "WHERE (:orderId IS NULL OR o.orderId = :orderId) " +
           "AND (:status IS NULL OR osh.status = :status) " +
           "AND (:memberId IS NULL OR o.memberId.memberId = :memberId) " +
           "AND (:customerId IS NULL OR o.customerId.customerId = :customerId) " +
           "AND (:numberOfPersons IS NULL OR o.numberOfPersons = :numberOfPersons) " +
           "AND (:room IS NULL OR o.room.roomId = :room) " +
           "AND (:orderDate IS NULL OR o.orderDate = :orderDate) " +
           "AND (:hours IS NULL OR o.hours = :hours) " + 
           "AND EXISTS (SELECT 1 FROM OrdersStatusHistory osh " +
           "            WHERE osh.orderId = o " +
           "            AND osh.status = :status " +
           "            AND osh.createTime = (SELECT MAX(osh2.createTime) " +
           "                                 FROM OrdersStatusHistory osh2 " +
		   "                                 WHERE osh2.orderId = o))")
    Page<Orders> findByConditions(@Param("status") String status,
                                  @Param("orderId") Long orderId,
                                  @Param("memberId") Integer memberId,
                                  @Param("customerId") Integer customerId,
                                  @Param("numberOfPersons") Integer numberOfPersons,
                                  @Param("room") Integer room,
                                  @Param("orderDate") Date orderDate,
                                  @Param("hours") Integer hours,
                                  Pageable pageable);
   // 無status條件搜尋   
   @Query("SELECT o FROM Orders o " +
	       "WHERE (:orderId IS NULL OR o.orderId = :orderId) " +
	       "AND (:memberId IS NULL OR o.memberId.memberId = :memberId) " +
	       "AND (:customerId IS NULL OR o.customerId.customerId = :customerId) " +
	       "AND (:numberOfPersons IS NULL OR o.numberOfPersons = :numberOfPersons) " +
	       "AND (:room IS NULL OR o.room.roomId = :room) " +
	       "AND (:orderDate IS NULL OR o.orderDate = :orderDate) " +
	       "AND (:hours IS NULL OR o.hours = :hours)")
	Page<Orders> findByConditionsWithoutStatus(@Param("orderId") Long orderId,
	                                           @Param("memberId") Integer memberId,
	                                           @Param("customerId") Integer customerId,
	                                           @Param("numberOfPersons") Integer numberOfPersons,
	                                           @Param("room") Integer room,
	                                           @Param("orderDate") Date orderDate,
	                                           @Param("hours") Integer hours,
	                                           Pageable pageable);
   
   @Override
   Page<Orders> findAll(Pageable pageable);
   
   // 有條件分頁
   @Query("SELECT COUNT(o) FROM Orders o LEFT JOIN o.ordersStatusHistory osh " +
	       "WHERE (:orderId IS NULL OR o.orderId = :orderId) " +
	       "AND (:status IS NULL OR osh.status = :status) " +
	       "AND (:memberId IS NULL OR o.memberId.memberId = :memberId) " +
	       "AND (:customerId IS NULL OR o.customerId.customerId = :customerId) " +
	       "AND (:numberOfPersons IS NULL OR o.numberOfPersons = :numberOfPersons) " +
	       "AND (:room IS NULL OR o.room.roomId = :room) " +
	       "AND (:orderDate IS NULL OR o.orderDate = :orderDate) " +
	       "AND (:hours IS NULL OR o.hours = :hours) " +
	       "AND EXISTS (SELECT 1 FROM OrdersStatusHistory osh2 " +
	       "            WHERE osh2.orderId = o " +
	       "            AND osh2.status = :status " +
	       "            AND osh2.createTime = (SELECT MAX(osh3.createTime) " +
	       "                                 FROM OrdersStatusHistory osh3 " +
	       "                                 WHERE osh3.orderId = o))")
	long countByConditions(@Param("status") String status,
	                       @Param("orderId") Long orderId,
	                       @Param("memberId") Integer memberId,
	                       @Param("customerId") Integer customerId,
	                       @Param("numberOfPersons") Integer numberOfPersons,
	                       @Param("room") Integer room,
	                       @Param("orderDate") Date orderDate,
	                       @Param("hours") Integer hours);
   
   // 無條件分頁
   @Query("SELECT COUNT(o) FROM Orders o " +
	       "WHERE (:orderId IS NULL OR o.orderId = :orderId) " +
	       "AND (:memberId IS NULL OR o.memberId.memberId = :memberId) " +
	       "AND (:customerId IS NULL OR o.customerId.customerId = :customerId) " +
	       "AND (:numberOfPersons IS NULL OR o.numberOfPersons = :numberOfPersons) " +
	       "AND (:room IS NULL OR o.room.roomId = :room) " +
	       "AND (:orderDate IS NULL OR o.orderDate = :orderDate) " +
	       "AND (:hours IS NULL OR o.hours = :hours)")
	long countByConditionsWithoutStatus(@Param("orderId") Long orderId,
	                                    @Param("memberId") Integer memberId,
	                                    @Param("customerId") Integer customerId,
	                                    @Param("numberOfPersons") Integer numberOfPersons,
	                                    @Param("room") Integer room,
	                                    @Param("orderDate") Date orderDate,
	                                    @Param("hours") Integer hours);
   
   
   
   
}
