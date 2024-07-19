package com.ispan.ktv.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Staff;
import com.ispan.ktv.repository.StaffRepository;
import com.ispan.ktv.util.DatetimeConverter;

@Service
public class StaffService {

	@Autowired
	private StaffRepository sr;

	public List<Staff> find() {

		return sr.findAll();
	}

	public List<Staff> findByName(String name) {
		return sr.findByName('%' + name + '%');
	}

	public Staff findByAccount(Integer account) {
		if (account != null) {
			Optional<Staff> optional = sr.findByAccount(account);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	public Staff findById(Integer id) {
		if (id != null) {
			Optional<Staff> optional = sr.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	public Staff create(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			String name = obj.isNull("name") ? null : obj.getString("name");
			Integer account = obj.isNull("account") ? null : obj.getInt("account");
			String password = obj.isNull("password") ? null : obj.getString("password");
			Integer status = obj.isNull("status") ? null : obj.getInt("status");
			String createBy = obj.isNull("createBy") ? null : obj.getString("createBy");
			String createTime = obj.isNull("createTime") ? null : obj.getString("createTime");
			Staff insert = new Staff();
			insert.setAccountName(name);
			insert.setAccount(account);
			insert.setPassword(password);
			insert.setStatus(status);
			insert.setCreateBy(createBy);
			insert.setCreateTime(null);
			insert.setCreateTime(DatetimeConverter.parse(createTime, "yyyy-MM-dd"));
			return sr.save(insert);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean exists(Integer id) {
		if (id != null) {
			return sr.existsById(id);
		}
		return false;
	}

	public Staff Update(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			Integer id = obj.isNull("Id") ? null : obj.getInt("Id");
			Staff bean = findById(id);
			String name = obj.isNull("name") ? null : obj.getString("name");
			Integer account = obj.isNull("account") ? null : obj.getInt("account");
			String password = obj.isNull("password") ? null : obj.getString("password");
			Integer status = obj.isNull("status") ? null : obj.getInt("status");
			String createBy = bean.getCreateBy();
			// String createBy = obj.isNull("createBy") ? null : obj.getString("createBy");
			Date createTime = bean.getCreateTime();
			// String createTime = obj.isNull("createTime") ? null : obj.getString("createTime");
			String updateBy = obj.isNull("updateBy") ? null : obj.getString("updateBy");
			Date updateTime = new Date();

			Optional<Staff> optional = sr.findById(id);
			if (optional.isPresent()) {
				Staff update = new Staff();
				update.setAccountId(id);
				update.setAccountName(name);
				update.setAccount(account);
				update.setPassword(password);
				update.setStatus(status);
				update.setCreateBy(createBy);
				update.setCreateTime(createTime);
				// update.setCreateTime(DatetimeConverter.parse(createTime, "yyyy-MM-dd"));
				update.setUpdateBy(updateBy);
				update.setUpdateTime(updateTime);
				return sr.save(update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Staff login(Integer account, String password) {
		if (account != null ) {
			Optional<Staff> optional = sr.findByAccount(account);

			if (optional.isPresent()) {
				if (password != null && password.length() != 0) {
					Staff bean = optional.get();

					byte[] pass = bean.getPassword().getBytes();
					byte[] temp = password.getBytes();
					if (Arrays.equals(pass, temp)) {
						return bean;
					}
				}
			}

		}
		return null;
	}
	
	public Staff changePassword(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			
			Integer account = obj.isNull("account") ? null : obj.getInt("account");
			Optional<Staff> optional1 = sr.findByAccount(account);
			Staff bean = optional1.get();
			Integer id = bean.getAccountId();
			String name = bean.getAccountName();
			String password = obj.isNull("newPassword") ? null : obj.getString("newPassword");
			Integer status = bean.getStatus();
			String createBy = bean.getCreateBy();
			Date createTime = bean.getCreateTime();
			String updateBy = bean.getAccountName();
			Date updateTime = new Date();

			Optional<Staff> optional = sr.findById(id);
			if (optional.isPresent()) {
				Staff update = new Staff();
				update.setAccountId(id);
				update.setAccountName(name);
				update.setAccount(account);
				update.setPassword(password);
				update.setStatus(status);
				update.setCreateBy(createBy);
				update.setCreateTime(createTime);
				update.setUpdateBy(updateBy);
				update.setUpdateTime(updateTime);
				return sr.save(update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

