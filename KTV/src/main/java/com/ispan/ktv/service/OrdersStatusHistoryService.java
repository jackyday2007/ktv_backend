package com.ispan.ktv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.bean.OrdersStatusHistory;
import com.ispan.ktv.bean.RoomHistory;
import com.ispan.ktv.repository.OrdersRepository;
import com.ispan.ktv.repository.OrdersStatusHistoryRepository;
import com.ispan.ktv.repository.RoomHistoryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdersStatusHistoryService {
	
	@Autowired
	private OrdersStatusHistoryRepository OrdersStatusHistoryRepo;
	
	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private RoomHistoryRepository roomHistoryRepository;
	
	
	
	public OrdersStatusHistory findNewHistory( Long ordersId ) {
		Optional<Orders> check = ordersRepository.findById(ordersId);
		if ( check != null ) {
			Optional<OrdersStatusHistory> optional = OrdersStatusHistoryRepo.history(check.get());
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	
	// 取消預約
	public OrdersStatusHistory noCheckIn(Long orderId) {
		Optional<Orders> optional = ordersRepository.findById(orderId);
		if ( optional.isPresent() ) {
			Orders update = optional.get();
			if ( update != null ) {
				RoomHistory roomHistory = new RoomHistory();
				OrdersStatusHistory history = new OrdersStatusHistory();
				history.setOrderId(update);
				history.setStatus("取消預約");
				roomHistory.setRoom(update.getRoom());
				roomHistory.setDate(update.getOrderDate());
				roomHistory.setStartTime(update.getStartTime());
				roomHistory.setEndTime(update.getEndTime());
				roomHistory.setStatus("取消預約");
				roomHistoryRepository.save(roomHistory);
				return OrdersStatusHistoryRepo.save(history);
			}
		}
		return null;
	}
	
	 // 取消預約
//    public OrdersStatusHistory noCheckIn(Long orderId) {
//        Optional<Orders> optional = ordersRepository.findById(orderId);
//        if (optional.isPresent()) {
//            Orders update = optional.get();
//            
//            // 查找是否已經有該訂單的取消記錄
//            Optional<OrdersStatusHistory> existingHistory = OrdersStatusHistoryRepo.history(update);
//            
//            OrdersStatusHistory history;
//            if (existingHistory.isPresent()) {
//                history = existingHistory.get();
//                // 更新已有記錄的狀態
//                history.setStatus("取消預約");
//            } else {
//                // 創建新的取消記錄
//                history = new OrdersStatusHistory();
//                history.setOrderId(update);
//                history.setStatus("取消預約");
//            }
//            
//            OrdersStatusHistory result = OrdersStatusHistoryRepo.save(history);
//            return result;
//        }
//        return null;
//    }


}
