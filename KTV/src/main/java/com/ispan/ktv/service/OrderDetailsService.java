package com.ispan.ktv.service;

import java.util.Optional;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.OrderDetails;
import com.ispan.ktv.bean.OrderMenus;
import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.repository.OrderDetailsRepository;
import com.ispan.ktv.repository.OrderMenusRepository;
import com.ispan.ktv.repository.OrdersRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderDetailsService {
	
	
	@Autowired
	OrderDetailsRepository OrderDetailsRepo;
	
	@Autowired
	OrdersRepository OrdersRepo;
	
	@Autowired
	OrderMenusRepository orderMenusRepo;
	
	
	public Double subTotal( Long orderId ) {
		if ( orderId != null ) {
			Optional<Orders> order = OrdersRepo.findById(orderId);
			if ( order.isPresent() ) {
				Optional<Double> orderDetails = OrderDetailsRepo.subTotal(order.get());
				if ( orderDetails.isPresent() ) {
					return orderDetails.get();
				}
			}
		}
		return null;
	}
	
	public OrderDetails insertDetail( String body ) {
		JSONObject obj = new JSONObject(body);
		Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
		Optional<Orders> optional = OrdersRepo.findById(orderId);
		if ( optional.isPresent() ) {
			String itemName = obj.isNull("itemName") ? null : obj.getString("itemName");
			Optional<OrderMenus> item = orderMenusRepo.findItemByName(itemName);
			System.out.println("item = " + item);
			if ( item.isPresent() ) {
				OrderDetails orderDetails = new OrderDetails();
				Integer quantity = obj.isNull("quantity") ? null : obj.getInt("quantity");
				Double subTotal = item.get().getPrice() * quantity;
				String OrderDetailId = randomNumber(6);
				orderDetails.setOrderDetailId(Integer.valueOf(OrderDetailId));
				orderDetails.setOrderId(optional.get());
				orderDetails.setItem(item.get().getItemName());
				orderDetails.setQuantity(quantity);
				orderDetails.setSubTotal(subTotal);
				return OrderDetailsRepo.save(orderDetails);
			}
		}
		return null;
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
