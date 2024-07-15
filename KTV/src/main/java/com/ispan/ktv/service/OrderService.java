package com.ispan.ktv.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.repository.OrdersRepository;
import com.ispan.ktv.repository.RoomsRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	OrdersRepository ordersRepository;

	@Autowired
	RoomsRepository roomsRepository;

	public Orders findByOrdersId(Long ordersId) {
		if (ordersId != null) {
			Optional<Orders> optional = ordersRepository.findById(ordersId);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	public List<Orders> findAll(String orders) {
		return ordersRepository.findAll();
	}

	@Transactional
	public Orders createOrders(Orders orders) {
		if (orders.getOrderId() != null) {
			return ordersRepository.save(orders);
		}
		return null;
	}
	

	// 產生訂單編號
	public String generateOrderId() {
		LocalDate today = LocalDate.now();
		String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		long sequenceNumber = getTodayOrderCount() + 1;
		String orderId = datePart + String.format("%03d", sequenceNumber);
		Long check = Long.valueOf(orderId);
		Orders find = findByOrdersId(check);
		if (find == null) {
			return datePart + String.format("%03d", sequenceNumber);
		} else {
			if (check == find.getOrderId()) {
				return String.valueOf(check + 1L);
			} else {
				return datePart + String.format("%03d", sequenceNumber);
			}
		}

	}

	// 計算今日訂單數量
	private long getTodayOrderCount() {
		System.out.println("Date: "+java.sql.Date.valueOf(LocalDate.now()));
		System.out.println("count: "+ordersRepository.countByOrderDate(java.sql.Date.valueOf((LocalDate.now()))));
		return ordersRepository.countByOrderDate(java.sql.Date.valueOf((LocalDate.now())));
	}

}
