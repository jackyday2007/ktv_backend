package com.ispan.ktv.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.OrderMenus;
import com.ispan.ktv.repository.OrderMenusRepository;
import com.ispan.ktv.util.DatetimeConverter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class OrderMenuService {
	
	@Autowired
	OrderMenusRepository OrderMenusRepo;
	
	
	// 算總筆數
	public Long count( String json ) {
		JSONObject body = new JSONObject(json);
		System.out.println(body);
		return OrderMenusRepo.count((root, query, criteriaBuilder) -> {
			List<Predicate> predicate = new ArrayList<>();
			
			if ( !body.isNull("itemId") ) {
				Integer itemId = body.getInt("itemId");
				predicate.add(criteriaBuilder.equal(root.get("itemId"), itemId));
			}
			
			if ( !body.isNull("category") ) {
				String category = body.getString("category");
				predicate.add(criteriaBuilder.equal(root.get("category"), category));
			}
			
			if ( !body.isNull("itemName") ) {
				String itemName = body.getString("itemName");
				predicate.add(criteriaBuilder.equal(root.get("itemName"), itemName));
			}
			
			if ( !body.isNull("capacity") ) {
				String capacity = body.getString("capacity") ;
				predicate.add(criteriaBuilder.equal(root.get("capacity"), capacity));
			}
			
			if ( !body.isNull("price") ) {
				Double price = body.getDouble("price") ;
				predicate.add(criteriaBuilder.equal(root.get("price"), price));
			}
			
			if ( !body.isNull("status") ) {
				String status = body.getString("status") ;
				predicate.add(criteriaBuilder.equal(root.get("status"), status));
			}
			
			query.where(predicate.toArray(new Predicate[0]));

			return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
		});
	}
	
	// 即時查詢
		public List<OrderMenus> find( String json ) {
			JSONObject body = new JSONObject(json);
			System.out.println("body=" + body);
			int start = body.isNull("start") ? 0 : body.getInt("start");
			int max = body.isNull("max") ? 5 : body.getInt("max");
			boolean dir = body.isNull("dir") ? false : body.getBoolean("dir");
			String order = body.isNull("order") ? "itemId" : body.getString("order");
			Sort sort = dir ? Sort.by(Sort.Direction.DESC, order) : Sort.by(Sort.Direction.ASC, order);
			Pageable pgb = PageRequest.of(start, max, sort);
			Specification<OrderMenus> spec = (Root<OrderMenus> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
				List<Predicate> predicate = new ArrayList<>();
				
				if ( !body.isNull("itemId") ) {
					Integer itemId = body.getInt("itemId");
					predicate.add(cb.equal(root.get("itemId"), itemId));
				}
				
				if ( !body.isNull("category") ) {
					String category = body.getString("category");
					predicate.add(cb.equal(root.get("category"), category));
				}
				
				if ( !body.isNull("itemName") ) {
					String itemName = body.getString("itemName");
					predicate.add(cb.like(root.get("itemName"), "%" + itemName + "%"));
				}
				
				if ( !body.isNull("capacity") ) {
					String capacity = body.getString("capacity") ;
					predicate.add(cb.equal(root.get("capacity"), capacity));
				}
				
				if ( !body.isNull("price") ) {
					Double price = body.getDouble("price") ;
					predicate.add(cb.equal(root.get("price"), price));
				}
				
				if ( !body.isNull("status") ) {
					Integer status = body.getInt("status") ;
					predicate.add(cb.equal(root.get("status"), status));
				}
				
				
				if ( !body.isNull("createTime") ) {
					String createTime = body.getString("createTime") ;
					predicate.add(cb.equal(root.get("createTime"), createTime));
				}
				
				
				if ( !body.isNull("createBy") ) {
					String createBy = body.getString("createBy") ;
					predicate.add(cb.equal(root.get("createBy"), createBy));
				}
				
				if ( !body.isNull("updateTime") ) {
					String updateTime = body.getString("updateTime") ;
					predicate.add(cb.equal(root.get("updateTime"), updateTime));
				}
				
				if ( !body.isNull("updateBy") ) {
					String updateBy = body.getString("updateBy") ;
					predicate.add(cb.equal(root.get("updateBy"), updateBy));
				}
				
				return cb.and(predicate.toArray(new Predicate[0]));
			};
			
			return OrderMenusRepo.findAll(spec, pgb).getContent();
		}
		
		
		
//		public OrderMenus 
		
		
		
		
		
		
		
		
		
		
		
	

	public OrderMenus Create(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String name = obj.isNull("itemName") ? null : obj.getString("itemName");
			String category = obj.isNull("category") ? null : obj.getString("category");
			String capacity = obj.isNull("capacity") ? null : obj.getString("capacity");
			Double  price = obj.isNull("price") ? null : obj.getDouble ("price");
			String status = obj.isNull("status") ? null : obj.getString("status");
			String createBy = obj.isNull("createBy") ? null : obj.getString("createBy");
			String createTime = obj.isNull("createTime") ? null : obj.getString("createTime");
			OrderMenus insert = new OrderMenus();
			insert.setItemName(name);
			insert.setCategory(category);
			insert.setCapacity(capacity);
			insert.setPrice(price);
			insert.setStatus(status);
			insert.setCreateBy(createBy);
			insert.setCreateTime(null);
			insert.setCreateTime(DatetimeConverter.parse(createTime, "yyyy-MM-dd"));
			return OrderMenusRepo.save(insert);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



}
