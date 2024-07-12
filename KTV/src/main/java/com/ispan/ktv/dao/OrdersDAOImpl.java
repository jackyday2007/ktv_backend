package com.ispan.ktv.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.ispan.ktv.bean.Orders;
import com.ispan.ktv.util.DatetimeConverter;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class OrdersDAOImpl implements OrdersDAO {
	
	@PersistenceContext
	private Session session;
	public Session getSession() {
		return this.session;
	}
	@Override
	public List<Orders> find(JSONObject obj) {
		
		System.out.println("obj: "+obj);
		
		Integer orderId = obj.isNull("orderId") ? null : obj.getInt("orderId");
		Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Integer memberId = obj.isNull("memberId") ? null : obj.getInt("memberId");
		Integer room = obj.isNull("room") ? null : obj.getInt("room");
		Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
		Integer hours = obj.isNull("hours") ? null : obj.getInt("hours");
		String orderDate = obj.isNull("orderDate") ? null : obj.getString("orderDate");
		String startTime = obj.isNull("startTime") ? null : obj.getString("startTime");
		String endTime = obj.isNull("endTime") ? null : obj.getString("endTime");
		Double subTotal = obj.isNull("subTotal") ? null : obj.getDouble("subTotal");
		
		int start = obj.isNull("start") ? 0 : obj.getInt("start");
		int max = obj.isNull("max") ? 5 : obj.getInt("max");
		boolean dir = obj.isNull("dir") ? false : obj.getBoolean("dir");
		String order = obj.isNull("order") ? "room" : obj.getString("room");
		
		CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<Orders> criteriaQuery = criteriaBuilder.createQuery(Orders.class);
		
		// from
		Root<Orders> table = criteriaQuery.from(Orders.class);
		
		// where
		List<Predicate> predicate = new ArrayList<>();
		if( orderId != null ) {
			predicate.add(criteriaBuilder.equal(table.get("orderId"), orderId));
		}
		
		if ( customerId != null ) {
			predicate.add(criteriaBuilder.equal(table.get("customerId"), customerId));
		}
		
		if ( memberId != null ) {
			predicate.add(criteriaBuilder.equal(table.get("memberId"), memberId));
		}
		
		if ( room != null ) {
			predicate.add(criteriaBuilder.equal(table.get("room"), room));
		}
		
		if ( numberOfPersons != null ) {
			predicate.add(criteriaBuilder.equal(table.get("numberOfPersons"), numberOfPersons));
		}
		
		if ( hours != null ) {
			predicate.add(criteriaBuilder.equal(table.get("hours"), hours));
		}
		
		if ( orderDate != null && orderDate.length() != 0 ) {
			Date date = DatetimeConverter.parse(orderDate, "yyyy-MM-dd");
			predicate.add(criteriaBuilder.equal(table.get("orderDate"), date));
		}
		
		if ( startTime != null && startTime.length() != 0 ) {
			Date time = DatetimeConverter.parse(startTime, "HH:mm:ss");
			predicate.add(criteriaBuilder.equal(table.get("startTime"), time));
		}
		
		if ( endTime != null && endTime.length() != 0 ) {
			Date time = DatetimeConverter.parse(endTime, "HH:mm:ss");
			predicate.add(criteriaBuilder.equal(table.get("endTime"), time));
		}
		
		if ( subTotal != null ) {
			predicate.add(criteriaBuilder.equal(table.get("subTotal"), subTotal));
		}
		
		// order by
		
		if( dir ) {
			criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.desc(table.get(order)));
		} else {
			criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.asc(table.get(order)));
		}
		
		TypedQuery<Orders> typedQuery = this.getSession().createQuery(criteriaQuery).setFirstResult(start).setMaxResults(max);
		
		List<Orders> result = typedQuery.getResultList();
		
		if ( result != null && result.isEmpty() ) {
			System.out.println("result: "+result);
			return result;
		} else {
			return null;
		}
		
	}
	
	@Override
	public long count( JSONObject obj ) {
		
		Integer orderId = obj.isNull("id") ? null : obj.getInt("id");
		Integer customerId = obj.isNull("customerId") ? null : obj.getInt("customerId");
		Integer memberId = obj.isNull("memberId") ? null : obj.getInt("memberId");
		Integer room = obj.isNull("room") ? null : obj.getInt("room");
		Integer numberOfPersons = obj.isNull("numberOfPersons") ? null : obj.getInt("numberOfPersons");
		Integer hours = obj.isNull("hours") ? null : obj.getInt("hours");
		String orderDate = obj.isNull("orderDate") ? null : obj.getString("orderDate");
		String startTime = obj.isNull("startTime") ? null : obj.getString("startTime");
		String endTime = obj.isNull("endTime") ? null : obj.getString("endTime");
		Double subTotal = obj.isNull("subTotal") ? null : obj.getDouble("subTotal"); 
		
		CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		// from
		Root<Orders> table = criteriaQuery.from(Orders.class);
		
		// select count(*)
		criteriaQuery = criteriaQuery.select(criteriaBuilder.count(table));
		
		// where
		List<Predicate> predicate = new ArrayList<>();
		
		if( orderId != null ) {
			predicate.add(criteriaBuilder.equal(table.get("orderId"), orderId));
		}
		
		if ( customerId != null ) {
			predicate.add(criteriaBuilder.equal(table.get("customerId"), customerId));
		}
		
		if ( memberId != null ) {
			predicate.add(criteriaBuilder.equal(table.get("memberId"), memberId));
		}
		
		if ( room != null ) {
			predicate.add(criteriaBuilder.equal(table.get("room"), room));
		}
		
		if ( numberOfPersons != null ) {
			predicate.add(criteriaBuilder.equal(table.get("numberOfPersons"), numberOfPersons));
		}
		
		if ( hours != null ) {
			predicate.add(criteriaBuilder.equal(table.get("hours"), hours));
		}
		
		if ( orderDate != null && orderDate.length() != 0 ) {
			Date date = DatetimeConverter.parse(orderDate, "yyyy-MM-dd");
			predicate.add(criteriaBuilder.equal(table.get("orderDate"), date));
		}
		
		if ( startTime != null && startTime.length() != 0 ) {
			Date time = DatetimeConverter.parse(startTime, "HH:mm:ss");
			predicate.add(criteriaBuilder.equal(table.get("startTime"), time));
		}
		
		if ( endTime != null && endTime.length() != 0 ) {
			Date time = DatetimeConverter.parse(endTime, "HH:mm:ss");
			predicate.add(criteriaBuilder.equal(table.get("endTime"), time));
		}
		
		if ( subTotal != null ) {
			predicate.add(criteriaBuilder.equal(table.get("subTotal"), subTotal));
		}
		
		criteriaQuery = criteriaQuery.where(predicate.toArray( new Predicate[0] ));
		
		TypedQuery<Long> typedQuery = this.getSession().createQuery(criteriaQuery);
		Long result = typedQuery.getSingleResult();
		if(result!=null) {
			return result;			
		} else {
			return 0;
		}
		
	}
	
}
