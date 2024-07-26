package com.ispan.ktv.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Customers;
import com.ispan.ktv.bean.Members;
import com.ispan.ktv.bean.OrderDetails;
import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.bean.OrdersStatusHistory;
import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.repository.CustomersRepository;
import com.ispan.ktv.repository.MembersRepository;
import com.ispan.ktv.repository.OrderDetailsRepository;
import com.ispan.ktv.repository.OrderMenusRepository;
import com.ispan.ktv.repository.OrdersRepository;
import com.ispan.ktv.repository.OrdersStatusHistoryRepository;
import com.ispan.ktv.repository.RoomsRepository;
import com.ispan.ktv.util.DatetimeConverter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
@Transactional
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
	
	@Autowired
	OrderDetailsRepository orderDetailsRepo;
	
	@Autowired
	OrderMenusRepository orderMenusRepository;
	
	
	
	public Orders findByOrdersId(Long ordersId) {
		if (ordersId != null) {
			Optional<Orders> optional = ordersRepository.findById(ordersId);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	
	// 算總筆數
	public Long count( String json ) {
		JSONObject body = new JSONObject(json);
		System.out.println(body);
		return ordersRepository.count((root, query, criteriaBuilder) -> {
			List<Predicate> predicate = new ArrayList<>();
			
			if ( !body.isNull("orderId") ) {
				Long orderId = body.getLong("orderId");
				predicate.add(criteriaBuilder.equal(root.get("orderId"), orderId));
			}
			
			if ( !body.isNull("memberId") ) {
				Integer memberId = body.getInt("memberId");
				predicate.add(criteriaBuilder.equal(root.get("memberId").get("memberId"), memberId));
			}
			
			if ( !body.isNull("customerId") ) {
				Integer customerId = body.getInt("customerId");
				predicate.add(criteriaBuilder.equal(root.get("customerId").get("customerId"), customerId));
			}
			
			if ( !body.isNull("room") ) {
				Integer room = body.getInt("room") ;
				predicate.add(criteriaBuilder.equal(root.get("room").get("roomId"), room));
			}
			
			if ( !body.isNull("hours") ) {
				Integer hours = body.getInt("hours") ;
				predicate.add(criteriaBuilder.equal(root.get("hours"), hours));
			}
			
			if ( !body.isNull("startTime") ) {
				String startTime = body.getString("startTime") ;
				predicate.add(criteriaBuilder.equal(root.get("startTime"), startTime));
			}
			
			if ( !body.isNull("endTime") ) {
				String endTime = body.getString("endTime") ;
				predicate.add(criteriaBuilder.equal(root.get("endTime"), endTime));
			}
			
			if ( !body.isNull("subTotal") ) {
				String subTotal = body.getString("subTotal") ;
				predicate.add(criteriaBuilder.equal(root.get("subTotal"), subTotal));
			}
			
			if ( !body.isNull("status") ) {
				String orderStatus = body.getString("status") ;
				Join<Orders, OrdersStatusHistory> historyJoin = root.join("ordersStatusHistory", JoinType.LEFT);
				predicate.add(criteriaBuilder.like(historyJoin.get("status"), "%" + orderStatus + "%"));
			}
			query.where(predicate.toArray(new Predicate[0]));

			return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
		});
	}
	
	// null的總數
	public Long countOrderDate( String json ) {
		JSONObject body = new JSONObject(json);
		System.out.println(body);
		return ordersRepository.count((root, query, criteriaBuilder) -> {
			List<Predicate> predicate = new ArrayList<>();
			
			if ( !body.isNull("orderDate") ) {
				String orderDate = body.getString("orderDate") ;
				predicate.add(criteriaBuilder.equal(root.get("orderDate"), orderDate));
			} else {
				predicate.add(criteriaBuilder.isNull(root.get("orderDate")));
			}
			query.where(predicate.toArray(new Predicate[0]));

			return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
		});
	}
	
	
	
	
	// 即時查詢
	public List<Orders> find( String json ) {
		JSONObject body = new JSONObject(json);
		System.out.println("body=" + body);
		int start = body.isNull("start") ? 0 : body.getInt("start");
		int max = body.isNull("max") ? 5 : body.getInt("max");
		boolean dir = body.isNull("dir") ? false : body.getBoolean("dir");
		String order = body.isNull("order") ? "orderId" : body.getString("order");
		Sort sort = dir ? Sort.by(Sort.Direction.DESC, order) : Sort.by(Sort.Direction.ASC, order);
		Pageable pgb = PageRequest.of(start, max, sort);
		Specification<Orders> spec = (Root<Orders> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
			List<Predicate> predicate = new ArrayList<>();
			
			if ( !body.isNull("orderId") ) {
				Long orderId = body.getLong("orderId");
				predicate.add(cb.equal(root.get("orderId"), orderId));
			}
			
			if ( !body.isNull("memberId") ) {
				Integer memberId = body.getInt("memberId");
				predicate.add(cb.equal(root.get("memberId").get("memberId"), memberId));
			}
			
			if ( !body.isNull("customerId") ) {
				Integer customerId = body.getInt("customerId");
				predicate.add(cb.equal(root.get("customerId").get("customerId"), customerId));
			}
			
			if ( !body.isNull("room") ) {
				Integer room = body.getInt("room") ;
				predicate.add(cb.equal(root.get("room").get("roomId"), room));
			}
			
			if ( !body.isNull("orderDate") ) {
				String orderDate = body.getString("orderDate") ;
				predicate.add(cb.equal(root.get("orderDate"), orderDate));
			}
			
			if ( !body.isNull("hours") ) {
				Integer hours = body.getInt("hours") ;
				predicate.add(cb.equal(root.get("hours"), hours));
			}
			
			if ( !body.isNull("startTime") ) {
				String startTime = body.getString("startTime") ;
				predicate.add(cb.equal(root.get("startTime"), (startTime)));
			}
			
			if ( !body.isNull("endTime") ) {
				String endTime = body.getString("endTime") ;
				predicate.add(cb.equal(root.get("endTime"), endTime));
			}
			
			if ( !body.isNull("status") ) {
				String status = body.getString("status") ;
				Join<Orders, OrdersStatusHistory> historyJoin = root.join("ordersStatusHistory", JoinType.LEFT);
				predicate.add(cb.like(historyJoin.get("status"), "%" + status + "%"));
			}
			return cb.and(predicate.toArray(new Predicate[0]));
		};
		
		return ordersRepository.findAll(spec, pgb).getContent();
	}
	
	// 報到
	public Orders watting( String body ) {
		JSONObject obj = new JSONObject(body);
		Customers customerId = null;
		Members memberId = null;
		Rooms room = null;
		Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
		Integer findCustomerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Integer findRoom = obj.isNull("room") ? null : obj.getInt("room");
		Integer findMemberId = obj.isNull("memberId") ? null : obj.getInt("memberId");
		Optional<Customers> checkCustomerId = findCustomerId != null ? customersRepository.findById(findCustomerId) : Optional.empty();
		Optional<Members> checkMemberId = findMemberId != null ? membersRepository.findById(findMemberId) : Optional.empty();
		Optional<Rooms> checkRoom = findRoom != null ? roomsRepository.findById(findRoom) : Optional.empty();
		if ( checkMemberId.isPresent() ) {
				memberId = checkMemberId.get();
			} else {
				memberId = null;
			}
		if (checkCustomerId.isPresent()) {
				customerId = checkCustomerId.get();
			} else {
				customerId = null;
			}
		if ( checkRoom.isPresent() ) {
				room = checkRoom.get();
			} else {
				room = null;
			}
		Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
		Optional<Orders> optional = ordersRepository.findById(orderId);
		if ( optional.isPresent() ) {
			Orders update = optional.get();
			update.setCustomerId(customerId);
			update.setMemberId(memberId);
			update.setRoom(room);
			update.setNumberOfPersons(numberOfPersons);
			Orders result =  ordersRepository.save(update);
			if (result.getOrderId() != null) {
				OrdersStatusHistory history = new OrdersStatusHistory();
				history.setOrderId(result);
				history.setStatus("報到");
				ordersStatusHistoryRepo.save(history);
				return result;
			}
		}
		return null;
	}
	
	
	// 新增預約
	public Orders updateOrders(String body) {
		JSONObject obj = new JSONObject(body);
		Customers customerId = null;
		Members memberId = null;
		Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
		Integer findCustomerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Optional<Customers> checkCustomerId = findCustomerId != null ? customersRepository.findById(findCustomerId) : Optional.empty();
		if (checkCustomerId.isPresent()) {
			customerId = checkCustomerId.get();
		} else {
			customerId = null;
		}
		Integer findMemberId = obj.isNull("memberId") ? null : obj.getInt("memberId");
		Optional<Members> checkMemberId = findMemberId != null ? membersRepository.findById(findMemberId) : Optional.empty();
		if ( checkMemberId.isPresent() ) {
			memberId = checkMemberId.get();
		} else {
			memberId = null;
		}
		Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
		String orderDate = obj.isNull("orderDate") ? null : obj.getString("orderDate");
		Integer hours = obj.isNull("hours") ? null : obj.getInt("hours");
		String startTime = obj.isNull("startTime") ? null : obj.getString("startTime");
		Optional<Orders> optional = ordersRepository.findById(orderId);
		if ( optional.isPresent() ) {
			Orders update = optional.get();
			update.setCustomerId(customerId);
			update.setMemberId(memberId);
			update.setNumberOfPersons(numberOfPersons);
			update.setOrderDate(DatetimeConverter.parse(orderDate, "yyyy-MM-dd"));
			update.setHours(hours);
			update.setStartTime(DatetimeConverter.parse(startTime, "HH:mm"));
			if ( startTime != null && hours != null ) {	
				LocalTime start = LocalTime.parse(startTime);
	            LocalTime end = start.plusHours(hours);
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	            String endTimeString = end.format(formatter);
	            update.setEndTime(DatetimeConverter.parse(endTimeString, "HH:mm"));
			}
			Orders result =  ordersRepository.save(update);
			OrdersStatusHistory history = new OrdersStatusHistory();
			if ( result.getOrderId() != null ) {
				history.setOrderId(result);
				history.setStatus("預約");
				ordersStatusHistoryRepo.save(history);
				return result;
			}
		}
		return null;
	}
	
	// 入場
	public Orders inTheRoom(String body) {
		JSONObject obj = new JSONObject(body);
		Members memberId = null;
		Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
		Integer findMemberId = obj.isNull("memberId") ? null : obj.getInt("memberId");
		Integer findRoom = obj.isNull("room") ? null : obj.getInt("room");
		Optional<Members> checkMemberId = findMemberId != null ? membersRepository.findById(findMemberId) : Optional.empty();
		Optional<Rooms> room = findRoom != null ? roomsRepository.findById(findRoom) : Optional.empty();
		if ( checkMemberId.isPresent() ) {
			memberId = checkMemberId.get();
		} else {
			memberId = null;
		}
		Optional<Orders> optional = ordersRepository.findById(orderId);
		if ( optional.isPresent() ) {
			Orders update = optional.get();
			update.setMemberId(memberId);
			update.setRoom(room.get());
			Orders result =  ordersRepository.save(update);
			if ( result.getOrderId() != null ) {
				String OrderDetailId = randomNumber(6);
				OrdersStatusHistory history = new OrdersStatusHistory();
				OrderDetails orderDetails = new OrderDetails();
				Rooms roomStatus = room.get();
				history.setOrderId(result);
				history.setStatus("消費中");
				orderDetails.setOrderDetailId(Integer.valueOf(OrderDetailId));
				orderDetails.setOrderId(result);
				orderDetails.setItem("包廂費");
				orderDetails.setSubTotal(room.get().getPrice());
				roomStatus.setStatus("使用中");
				
				ordersStatusHistoryRepo.save(history);
				orderDetailsRepo.save(orderDetails);
				roomsRepository.save(roomStatus);
				return result;
			}
		}
		return null;
	}
	
	@Transactional
	public Orders createOrderId( Long id ) {
		Orders order = new Orders();
		order.setOrderId(id);
		return ordersRepository.save(order);
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
		return ordersRepository.countByCreateTime(java.sql.Date.valueOf((LocalDate.now())));
	}
	
	// 產生亂數編號
	private static final String NUMBERS = "0123456789";
    
	private static String randomNumber(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        // 循環生成指定長度的隨機數字字符串
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(NUMBERS.length());
            stringBuilder.append(NUMBERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

}
