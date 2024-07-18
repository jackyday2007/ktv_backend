package com.ispan.ktv.dao;

import java.util.List;

import org.json.JSONObject;

import com.ispan.ktv.bean.Orders;

public interface OrdersDAO {

	public abstract List<Orders> find( JSONObject obj );
	

	public abstract long count(JSONObject obj);
	
}
