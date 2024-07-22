package com.ispan.ktv.service;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Customers;
import com.ispan.ktv.repository.CustomersRepository;
import com.ispan.ktv.util.DatetimeConverter;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
	
	@Autowired
	CustomersRepository customersRepository;
	
	public Customers findCustomerId( Integer id ) {
		if ( id != null ) {
			Optional<Customers> optional = customersRepository.findById(id);
			if ( optional.isPresent() ) {
				return optional.get();
			}
		}
		return null;
	}
	
	public Customers findIdNumber( String id ) {
		if ( id != null ) {
			Optional<Customers> optional = customersRepository.customerId(id);
			if ( optional.isPresent() ) {
				return optional.get();
			}
		}
		return null;
	}
	
	public Customers insertCustomer( String body ) {
		
		JSONObject obj = new JSONObject(body);
		String idNumber = obj.isNull("idNumber") ? null : obj.getString("idNumber");
		
		if ( idNumber != null ) {
			Customers customers = new Customers();
			customers.setIdNumber(idNumber);
			customers.setName(obj.isNull("name") ? null : obj.getString("name"));
			customers.setBirth(obj.isNull("birth") ? null : DatetimeConverter.parse("birth", "yyyy-MM-dd"));
			customers.setPhone(obj.isNull("phone") ? null : obj.getInt("phone") );
			return customersRepository.save(customers);
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
