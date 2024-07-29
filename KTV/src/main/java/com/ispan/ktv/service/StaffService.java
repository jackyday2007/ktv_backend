package com.ispan.ktv.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ispan.ktv.bean.Staff;
import com.ispan.ktv.repository.StaffRepository;
import com.ispan.ktv.util.DatetimeConverter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class StaffService {

	@Autowired
	private StaffRepository sr;

	// @Autowired
    // private BCryptPasswordEncoder bCryptPasswordEncoder;

	// 算總筆數
	public Long count( String json ) {
		JSONObject body = new JSONObject(json);
		System.out.println(body);
		return sr.count((root, query, criteriaBuilder) -> {
			List<Predicate> predicate = new ArrayList<>();
			if ( !body.isNull("accountId") ) {
				Integer accountId = body.getInt("accountId");
				predicate.add(criteriaBuilder.equal(root.get("accountId"), accountId));
			}
			if ( !body.isNull("account") ) {
				Integer account = body.getInt("account");
				predicate.add(criteriaBuilder.equal(root.get("account"), account));
			}
			
			if ( !body.isNull("accountName") ) {
				String accountName = body.getString("accountName");
				predicate.add(criteriaBuilder.equal(root.get("accountName"), accountName));
			}
			
			if ( !body.isNull("password") ) {
				String password = body.getString("password") ;
				predicate.add(criteriaBuilder.equal(root.get("password"), password));
			}
			
			
			if ( !body.isNull("status") ) {
				String status = body.getString("status") ;
				predicate.add(criteriaBuilder.equal(root.get("status"), status));
			}
			
			query.where(predicate.toArray(new Predicate[0]));

			return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
		});
	}



	public List<Staff> find(String json) {

		JSONObject body = new JSONObject(json);
			System.out.println("body=" + body);
			int start = body.isNull("start") ? 0 : body.getInt("start");
			int max = body.isNull("max") ? 5 : body.getInt("max");
			boolean dir = body.isNull("dir") ? false : body.getBoolean("dir");
			String order = body.isNull("order") ? "itemId" : body.getString("order");
			Sort sort = dir ? Sort.by(Sort.Direction.DESC, order) : Sort.by(Sort.Direction.ASC, order);
			Pageable pgb = PageRequest.of(start, max, sort);
			Specification<Staff> spec = (Root<Staff> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
				List<Predicate> predicate = new ArrayList<>();
				
				if ( !body.isNull("accountId") ) {
					Integer accountId = body.getInt("accountId");
					predicate.add(cb.equal(root.get("accountId"), accountId));
				}
				if ( !body.isNull("account") ) {
					Integer account = body.getInt("account");
					predicate.add(cb.equal(root.get("account"), account));
				}
				
				if ( !body.isNull("accountName") ) {
					String accountName = body.getString("accountName");
					predicate.add(cb.equal(root.get("accountName"), accountName));
				}
				
				if ( !body.isNull("password") ) {
					String password = body.getString("password") ;
					predicate.add(cb.equal(root.get("password"), password));
				}
				
				
				if ( !body.isNull("status") ) {
					String status = body.getString("status") ;
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
			
			return sr.findAll(spec, pgb).getContent();
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

	public Staff create(String json ) {
		try {
			JSONObject obj = new JSONObject(json);
			String name = obj.isNull("name") ? null : obj.getString("name");
			Integer account = obj.isNull("account") ? null : obj.getInt("account");
			String password = obj.isNull("password") ? null : obj.getString("password");
			Integer status = obj.isNull("status") ? null : obj.getInt("status");
			String createBy = obj.isNull("createBy") ? null : obj.getString("createBy");
			String createTime = obj.isNull("createTime") ? null : obj.getString("createTime");
			// 加密密码
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String encryptedPassword = password != null ? bCryptPasswordEncoder.encode(password) : null;

			Staff insert = new Staff();
			insert.setAccountName(name);
			insert.setAccount(account);
			insert.setPassword(encryptedPassword);
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

	public boolean exists(Integer account) {
		Optional<Staff> bean = sr.findByAccount(account);
		if (account != null && bean.isPresent()) {
			return true;
		}
		return false;
	}

	public Staff Update(String json) {
		// System.out.println("json"+json);
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
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String encryptedPassword = password != null ? bCryptPasswordEncoder.encode(password) : null;
			Optional<Staff> optional = sr.findById(id);
			if (optional.isPresent()) {
				Staff update = new Staff();
				update.setAccountId(id);
				update.setAccountName(name);
				update.setAccount(account);
				update.setPassword(encryptedPassword);
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
					BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
					byte[] pass = bean.getPassword().getBytes();
					byte[] temp = password.getBytes();
					boolean result = bCryptPasswordEncoder.matches(password, bean.getPassword());
					if (Arrays.equals(pass, temp) || result) {
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

