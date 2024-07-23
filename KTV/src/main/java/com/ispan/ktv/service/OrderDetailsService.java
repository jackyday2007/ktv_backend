package com.ispan.ktv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.json.JSONArray;
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
	OrderDetailsRepository orderDetailsRepo;
	
	@Autowired
	OrdersRepository OrdersRepo;
	
	@Autowired
	OrderMenusRepository orderMenusRepo;
	
	
	public Double subTotal( Long orderId ) {
		if ( orderId != null ) {
			Optional<Orders> order = OrdersRepo.findById(orderId);
			if ( order.isPresent() ) {
				Optional<Double> orderDetails = orderDetailsRepo.subTotal(order.get());
				if ( orderDetails.isPresent() ) {
					return orderDetails.get();
				}
			}
		}
		return null;
	}
	
	public List<OrderDetails> insertNewDetail( String body ) {
		List<OrderDetails> orderDetailsList = new ArrayList<>();
	    JSONArray array = new JSONArray(body);
		Integer orderDetailId = Integer.valueOf(randomNumber(6));
	    for ( int i = 0; i < array.length(); i++ ) {
	    	JSONObject obj = array.getJSONObject(i);
	    	Long orderId = obj.isNull("orderId") ? null : obj.getLong("orderId");
			String itemName = obj.isNull("itemName") ? null : obj.getString("itemName");
			Integer quantity = obj.isNull("quantity") ? null : obj.getInt("quantity");
			Optional<Orders> order = OrdersRepo.findById(orderId);
			if ( order.isPresent() ) {
				Optional<OrderMenus> item = orderMenusRepo.findByItemName(itemName);
				if ( item.isPresent() ) {
					OrderDetails orderDetails = new OrderDetails();
					orderDetails.setOrderDetailId(orderDetailId);
					orderDetails.setOrderId(order.get());
					orderDetails.setItem(item.get().getItemName());
					orderDetails.setQuantity(quantity);
					orderDetails.setSubTotal(item.get().getPrice() * quantity);
					OrderDetails answer = orderDetailsRepo.save(orderDetails);
					if ( answer != null ) {
						orderDetailsList.add(answer);
					}
				}
			}
	    }
		return orderDetailsList;
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

}
