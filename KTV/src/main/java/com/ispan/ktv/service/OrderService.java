package com.ispan.ktv.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Customers;
import com.ispan.ktv.bean.Members;
import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.bean.OrdersStatusHistory;
import com.ispan.ktv.repository.CustomersRepository;
import com.ispan.ktv.repository.MembersRepository;
import com.ispan.ktv.repository.OrdersRepository;
import com.ispan.ktv.repository.RoomsRepository;
import com.ispan.ktv.repository.OrdersStatusHistoryRepository;
import com.ispan.ktv.util.DatetimeConverter;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	OrdersRepository ordersRepository;

	@Autowired
	RoomsRepository roomsRepository;
	
	@Autowired
	CustomersRepository customersRepository;
	
	@Autowired
	MembersRepository membersRepository;
	
	@Autowired
	OrdersStatusHistoryRepository ordersStatusHistoryRepo;
	

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
	public Orders updateOrders(String body) {
		JSONObject obj = new JSONObject(body);
		Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
		Integer findCustomerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Optional<Customers> checkCustomerId = customersRepository.findById(findCustomerId);
		Customers customerId = checkCustomerId.get();
		Integer findMemberId = obj.isNull("memberId") ? null : obj.getInt("memberId");
		Optional<Members> checkMemberId = membersRepository.findById(findMemberId);
		Members memberId = checkMemberId.get();
		Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
		String orderDate = obj.isNull("orderDate") ? null : obj.getString("orderDate");
		Integer hours = obj.isNull("hours") ? null : obj.getInt("hours");
		String startTime = obj.isNull("startTime") ? null : obj.getString("startTime");
		String endTime = obj.isNull("endTime") ? null : obj.getString("endTime");
		Optional<Orders> optional = ordersRepository.findById(orderId);
		if ( optional.isPresent() ) {
			Orders update = optional.get();
			update.setCustomerId(customerId);
			update.setMemberId(memberId);
			update.setNumberOfPersons(numberOfPersons);
			update.setOrderDate(DatetimeConverter.parse(orderDate, "yyyy-MM-dd"));
			update.setHours(hours);
			update.setStartTime(DatetimeConverter.parse(startTime, "HH:mm"));
			update.setEndTime(DatetimeConverter.parse(endTime, "HH:mm"));
			Orders result =  ordersRepository.save(update);
			if ( result.getOrderId() != null ) {
				OrdersStatusHistory history = new OrdersStatusHistory();
				history.setOrderId(result);
				history.setStatus("預約");
				ordersStatusHistoryRepo.save(history);
				return result;
			}
		}
		return null;
	}
	
	
	@Transactional
	public Orders createOrderId( Long id ) {
		Orders orderId = new Orders();
		orderId.setOrderId(id);
		return ordersRepository.save(orderId);
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
		return ordersRepository.countByOrderDate(java.sql.Date.valueOf((LocalDate.now())));
	}

}
