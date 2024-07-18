package com.ispan.ktv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Customers;
import com.ispan.ktv.repository.CustomersRepository;

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
	
	
	
	
	
}
