package com.ispan.ktv.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Customers;
import com.ispan.ktv.bean.Members;
import com.ispan.ktv.bean.OrderDetails;
import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.bean.OrdersStatusHistory;
import com.ispan.ktv.bean.RoomHistory;
import com.ispan.ktv.bean.Rooms;
import com.ispan.ktv.repository.CustomersRepository;
import com.ispan.ktv.repository.MembersRepository;
import com.ispan.ktv.repository.OrderDetailsRepository;
import com.ispan.ktv.repository.OrdersRepository;
import com.ispan.ktv.repository.OrdersStatusHistoryRepository;
import com.ispan.ktv.repository.RoomHistoryRepository;
import com.ispan.ktv.repository.RoomsRepository;
import com.ispan.ktv.util.DatetimeConverter;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private RoomsRepository roomsRepository;

	@Autowired
	private CustomersRepository customersRepository;

	@Autowired
	private MembersRepository membersRepository;

	@Autowired
	private OrdersStatusHistoryRepository ordersStatusHistoryRepo;

	@Autowired
	private OrderDetailsRepository orderDetailsRepo;
	
	@Autowired
	private RoomHistoryRepository roomHistoryRepository;
	
	public Orders findByOrdersId(Long ordersId) {
		if (ordersId != null) {
			Optional<Orders> optional = ordersRepository.findById(ordersId);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	
	// 即時查詢
    public Page<Orders> findTest(String json) {
        JSONObject body = new JSONObject(json);
        int start = body.isNull("start") ? 0 : body.getInt("start");
        int max = body.isNull("max") ? 5 : body.getInt("max");
        boolean dir = body.isNull("dir") ? false : body.getBoolean("dir");
        String order = body.isNull("order") ? "orderId" : body.getString("order");
        Sort sort = dir ? Sort.by(Sort.Direction.DESC, order) : Sort.by(Sort.Direction.ASC, order);
        Pageable pageable = PageRequest.of(start, max, sort);
        String status = body.isNull("status") ? null : body.getString("status");
        Long orderId = body.isNull("orderId") ? null : body.getLong("orderId");
        Integer memberId = body.isNull("memberId") ? null : body.getInt("memberId");
        Integer customerId = body.isNull("customerId") ? null : body.getInt("customerId");
        Integer numberOfPersons = body.isNull("numberOfPersons") ? null : body.getInt("numberOfPersons");
        Integer room = body.isNull("room") ? null : body.getInt("room");
        Date orderDate = body.isNull("orderDate") ? null : DatetimeConverter.parse(body.getString("orderDate"), "yyyy-MM-dd");
        Integer hours = body.isNull("hours") ? null : body.getInt("hours");

        // 使用 Pageable 进行分页查询
        if (status == null) {
            // 如果 status 为 null，调用不带 status 参数的查询方法
        	return ordersRepository.findByConditionsWithoutStatus(orderId, memberId, customerId, numberOfPersons, room, orderDate, hours, pageable);
        } else {
            // 否则，调用带 status 参数的查询方法
            return ordersRepository.findByConditions(status, orderId, memberId, customerId, numberOfPersons, room, orderDate, hours, pageable);
        }
    }
    
    // 算總筆數
    public Long countTest(String json) {
        JSONObject body = new JSONObject(json);
        String status = body.isNull("status") ? null : body.getString("status");
        Long orderId = body.isNull("orderId") ? null : body.getLong("orderId");
        Integer memberId = body.isNull("memberId") ? null : body.getInt("memberId");
        Integer customerId = body.isNull("customerId") ? null : body.getInt("customerId");
        Integer numberOfPersons = body.isNull("numberOfPersons") ? null : body.getInt("numberOfPersons");
        Integer room = body.isNull("room") ? null : body.getInt("room");
        Date orderDate = body.isNull("orderDate") ? null : DatetimeConverter.parse(body.getString("orderDate"), "yyyy-MM-dd");
        Integer hours = body.isNull("hours") ? null : body.getInt("hours");
        if (status == null) {
            return ordersRepository.countByConditionsWithoutStatus(orderId, memberId, customerId, numberOfPersons, room, orderDate, hours);
        } else {
            return ordersRepository.countByConditions(status, orderId, memberId, customerId, numberOfPersons, room, orderDate, hours);
        }
    }
    
    // 新增訂單
    public Orders createNewOrder(String body) {
		Long orderId = Long.valueOf(generateOrderId());
		Orders order = new Orders();
		order.setOrderId(orderId);
		Orders result = ordersRepository.save(order);
		if (result != null) {
			Optional<Orders> optional = ordersRepository.findById(orderId);
			if (optional.isPresent()) {
				JSONObject obj = new JSONObject(body);
				Integer findCustomerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
				Integer findMemberId = obj.isNull("memberId") ? null : obj.getInt("memberId");
				Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
				String orderDate = obj.isNull("orderDate") ? null : obj.getString("orderDate");
				Date date = convertStringToDate(orderDate);
				Integer hours = obj.isNull("hours") ? null : obj.getInt("hours");
				String startTime = obj.isNull("startTime") ? null : obj.getString("startTime");
				String endTimeString = null;
				if (startTime != null && hours != null) {
					LocalTime start = LocalTime.parse(startTime);
					LocalTime end = start.plusHours(hours);
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
					endTimeString = end.format(formatter);
				}
				Customers customerId = null;
				Members memberId = null;
				Optional<Customers> checkCustomerId = findCustomerId != null ? customersRepository.findById(findCustomerId) : Optional.empty();
				if (checkCustomerId.isPresent()) {
					customerId = checkCustomerId.get();
				} else {
					customerId = null;
				}
				Optional<Members> checkMemberId = findMemberId != null ? membersRepository.findById(findMemberId) : Optional.empty();
				if (checkMemberId.isPresent()) {
					memberId = checkMemberId.get();
				} else {
					memberId = null;
				}
				Orders orders = optional.get();
				orders.setCustomerId(customerId);
				orders.setMemberId(memberId);
				orders.setNumberOfPersons(numberOfPersons);
				orders.setOrderDate(DatetimeConverter.parse(orderDate, "yyyy-MM-dd"));
				orders.setHours(hours);
				orders.setStartTime(DatetimeConverter.parse(startTime, "HH:mm"));
				orders.setEndTime(DatetimeConverter.parse(endTimeString, "HH:mm"));
				if ( memberId != null ) {
					orders.setCreateBy(String.valueOf(memberId.getMemberId()));
				}
				List<Rooms> rooms = null;
				if ( numberOfPersons > 0 && numberOfPersons <= 6 ) {
					rooms = roomsRepository.findRoomSize("小");
				} else if ( numberOfPersons >= 7 && numberOfPersons <= 10 ) {
					rooms = roomsRepository.findRoomSize("中");
				} else {
					rooms = roomsRepository.findRoomSize("大");
				}
				for ( Rooms roomId : rooms ) {
					List<RoomHistory> histories = roomHistoryRepository.findRoomHistoryWhithDateAndRoom(date, roomId);
					if ( isRoomAvailable(histories, startTime, endTimeString) ) {
						RoomHistory roomHistory = new RoomHistory();
						roomHistory.setDate(date);
						roomHistory.setRoom(roomId);
						orders.setRoom(roomId);
						roomHistory.setStartTime(DatetimeConverter.parse(startTime, "HH:mm"));
						roomHistory.setEndTime(DatetimeConverter.parse(endTimeString, "HH:mm"));
						roomHistory.setStatus("預約");
						RoomHistory rh = roomHistoryRepository.save(roomHistory);
						if ( rh != null ) {
							break;
						}
					}
				}
				Orders answer = ordersRepository.save(orders);
				OrdersStatusHistory history = new OrdersStatusHistory();
				if (answer.getOrderId() != null) {
					history.setOrderId(answer);
					history.setStatus("預約");
					ordersStatusHistoryRepo.save(history);
				}
				return result;
			}
		}
		return null;
	}
    
    // 時間區段判斷 Start
    private boolean isRoomAvailable(List<RoomHistory> histories, String startTime, String endTime) {
        for (RoomHistory history : histories) {
            // 獲取已存在的預訂的開始時間和結束時間
            String existingStartTime = DatetimeConverter.toString(history.getStartTime(), "HH:mm");
            String existingEndTime = DatetimeConverter.toString(history.getEndTime(), "HH:mm");

            // 檢查時間區間是否重疊
            if (isTimeOverlap(startTime, endTime, existingStartTime, existingEndTime)) {
            	// 時間衝突，房間不可用
                return false;
            }
        }
        // 時間區間內沒有衝突，房間可用
        return true; 
    }

    private boolean isTimeOverlap(String startTime1, String endTime1, String startTime2, String endTime2) {
        // 轉換時間字符串為時間戳（可以選擇其他格式
        long start1 = parseTimeToTimestamp(startTime1);
        long end1 = parseTimeToTimestamp(endTime1);
        long start2 = parseTimeToTimestamp(startTime2);
        long end2 = parseTimeToTimestamp(endTime2);

        // 檢查是否重疊
        return start1 < end2 && end1 > start2;
    }
    
    private long parseTimeToTimestamp(String time) {
        // 示例：將時間字符串轉換為時間戳（毫秒）
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            return dateFormat.parse(time).getTime();
        } catch (ParseException e) {
            throw new RuntimeException("时间格式错误: " + time, e);
        }
    }

    // 時間區段判斷 End
    
	// 報到
	public Orders watting(String body) {
		JSONObject obj = new JSONObject(body);
		Customers customerId = null;
		Members memberId = null;
		Rooms room = null;
		Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
		Integer findCustomerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Integer findRoom = obj.isNull("room") ? null : obj.getInt("room");
		Integer findMemberId = obj.isNull("memberId") ? null : obj.getInt("memberId");
		Optional<Customers> checkCustomerId = findCustomerId != null ? customersRepository.findById(findCustomerId)
				: Optional.empty();
		Optional<Members> checkMemberId = findMemberId != null ? membersRepository.findById(findMemberId)
				: Optional.empty();
		Optional<Rooms> checkRoom = findRoom != null ? roomsRepository.findById(findRoom) : Optional.empty();
		if (checkMemberId.isPresent()) {
			memberId = checkMemberId.get();
		} else {
			memberId = null;
		}
		if (checkCustomerId.isPresent()) {
			customerId = checkCustomerId.get();
		} else {
			customerId = null;
		}
		if (checkRoom.isPresent()) {
			room = checkRoom.get();
		} else {
			room = null;
		}
		Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
		Optional<Orders> optional = ordersRepository.findById(orderId);
		if (optional.isPresent()) {
			Orders update = optional.get();
			update.setCustomerId(customerId);
			update.setMemberId(memberId);
			update.setRoom(room);
			update.setNumberOfPersons(numberOfPersons);
			Orders result = ordersRepository.save(update);
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

	// 修改預約
	public Orders updateOrders(String body) {
		JSONObject obj = new JSONObject(body);
		Customers customerId = null;
		Members memberId = null;
		Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
		Integer findCustomerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Optional<Customers> checkCustomerId = findCustomerId != null ? customersRepository.findById(findCustomerId)
				: Optional.empty();
		if (checkCustomerId.isPresent()) {
			customerId = checkCustomerId.get();
		} else {
			customerId = null;
		}
		Integer findMemberId = obj.isNull("memberId") ? null : obj.getInt("memberId");
		Optional<Members> checkMemberId = findMemberId != null ? membersRepository.findById(findMemberId)
				: Optional.empty();
		if (checkMemberId.isPresent()) {
			memberId = checkMemberId.get();
		} else {
			memberId = null;
		}
		Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
		String orderDate = obj.isNull("orderDate") ? null : obj.getString("orderDate");
		Integer hours = obj.isNull("hours") ? null : obj.getInt("hours");
		String startTime = obj.isNull("startTime") ? null : obj.getString("startTime");
		Optional<Orders> optional = ordersRepository.findById(orderId);
		if (optional.isPresent()) {
			Orders update = optional.get();
			update.setCustomerId(customerId);
			update.setMemberId(memberId);
			update.setNumberOfPersons(numberOfPersons);
			update.setOrderDate(DatetimeConverter.parse(orderDate, "yyyy-MM-dd"));
			update.setHours(hours);
			update.setStartTime(DatetimeConverter.parse(startTime, "HH:mm"));
			if (startTime != null && hours != null) {
				LocalTime start = LocalTime.parse(startTime);
				LocalTime end = start.plusHours(hours);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				String endTimeString = end.format(formatter);
				update.setEndTime(DatetimeConverter.parse(endTimeString, "HH:mm"));
			}
			if ( memberId != null ) {
				update.setUpdateBy(String.valueOf(memberId.getMemberId()));
				update.setUpdateTime(Date.from(Instant.now()));
			}
			return ordersRepository.save(update);
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
		Optional<Members> checkMemberId = findMemberId != null ? membersRepository.findById(findMemberId)
				: Optional.empty();
		Optional<Rooms> room = findRoom != null ? roomsRepository.findById(findRoom) : Optional.empty();
		if (checkMemberId.isPresent()) {
			memberId = checkMemberId.get();
		} else {
			memberId = null;
		}
		Optional<Orders> optional = ordersRepository.findById(orderId);
		if (optional.isPresent()) {
			Orders update = optional.get();
			update.setMemberId(memberId);
			update.setRoom(room.get());
			Orders result = ordersRepository.save(update);
			if (result.getOrderId() != null) {
				String OrderDetailId = randomNumber(6);
				OrdersStatusHistory history = new OrdersStatusHistory();
				OrderDetails orderDetails = new OrderDetails();
				Rooms roomStatus = room.get();
				history.setOrderId(result);
				history.setStatus("消費中");
				orderDetails.setOrderDetailId(Integer.valueOf(OrderDetailId));
				orderDetails.setOrderId(result);
				orderDetails.setPrice(room.get().getPrice());
				orderDetails.setItem("包廂費(" + room.get().getSize() + ")");
				orderDetails.setQuantity(1);
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
	public Orders createOrderId(Long id) {
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
	private static final String NUMBERS = "123456789";

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
	
	private static Date convertStringToDate(String dateString) {
        // 定义日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // 设置为严格解析模式

        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
    
	

}
