package com.ispan.ktv.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.ktv.bean.OrderMenus;
import com.ispan.ktv.service.OrderMenuService;
import com.ispan.ktv.util.DatetimeConverter;



@RestController
@CrossOrigin
public class OrderMenusController {
	
	@Autowired
	private OrderMenuService orderMenuService;
	
	
	@PostMapping("/orderMenu/allMenu")
	public String allMenu(@RequestBody(required = false) String body  ) {
		JSONObject responseBody = new JSONObject();
		List<OrderMenus> result = orderMenuService.find(body);
		Long count = orderMenuService.count(body);
		JSONArray array = new JSONArray();
		if ( result != null && !result.isEmpty() ) {
			for ( OrderMenus orderMenus : result ) {
				JSONObject item = new JSONObject()
				.put("itemId", orderMenus.getItemId())
				.put("category", orderMenus.getCategory())
				.put("itemName", orderMenus.getItemName())
				.put("capacity", orderMenus.getCapacity())
				.put("price", orderMenus.getPrice())
				.put("status", orderMenus.getStatus())
				.put("creater", orderMenus.getCreateBy())
				.put("createtime", orderMenus.getCreateTime())
				.put("updateBy", orderMenus.getUpdateBy())
				.put("updateTime", orderMenus.getUpdateTime());
				array.put(item);
			}
		}
		responseBody.put("count", count);
		responseBody.put("list", array);
		return responseBody.toString();
	}
	
	
	@GetMapping("/orderMenu/categoryList")
    public List<String> categoryList() {
        return orderMenuService.categoryByList();
    }
	
	
	
	@PostMapping("/orderMenu/creat")
	public String creat(@RequestBody String body) {
		JSONObject responseBody = new JSONObject();
		
		OrderMenus product = orderMenuService.Create(body);
		if (product == null) {
			responseBody.put("success", false);
			responseBody.put("message", "新增失敗");
		} else {
			responseBody.put("success", true);
			responseBody.put("message", "新增成功");
			}
		return responseBody.toString();
	}
	
	@GetMapping("/orderMenu/find/{id}")
	public String findById(@PathVariable(name = "id") Integer id) {

		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		OrderMenus orderMenus = orderMenuService.findById(id);
		if (id != null) {
			String createtime = DatetimeConverter.toString(orderMenus.getCreateTime(), "yy-MM-dd");
			String updateTime = DatetimeConverter.toString(orderMenus.getUpdateTime(), "yy-MM-dd");
			JSONObject item;
			
				item = new JSONObject()
				.put("itemId", orderMenus.getItemId())
				.put("category", orderMenus.getCategory())
				.put("itemName", orderMenus.getItemName())
				.put("capacity", orderMenus.getCapacity())
				.put("price", orderMenus.getPrice())
				.put("status", orderMenus.getStatus())
				.put("creater", orderMenus.getCreateBy())
				.put("createtime", createtime)
				.put("updateBy", orderMenus.getUpdateBy())
				.put("updateTime", updateTime);
				array = array.put(item);
				responseBody.put("list", array);
			
		}


		return responseBody.toString();
	}
	
	@PutMapping("/orderMenu/update/{pk}")
	public String update(@PathVariable(name = "pk") Integer id, @RequestBody String body) {
		JSONObject responseBody = new JSONObject();

			if (id == null) {
				responseBody.put("success", false);
				responseBody.put("message", "Id是必要欄位");
			} else {
				if (!orderMenuService.exists(id)) {
					responseBody.put("success", false);
					responseBody.put("message", "Id不存在");
				} else {
					OrderMenus product = orderMenuService.Update(body);
					if (product == null) {
						responseBody.put("success", false);
						responseBody.put("message", "修改失敗");
					} else {
						responseBody.put("success", true);
						responseBody.put("message", "修改成功");
					}
				}
			}

		return responseBody.toString();

	}
	@PutMapping("/orderMenu/changeStatus/{id}")
	public String changeStatus(@PathVariable(name = "id") Integer id, @RequestBody String body) {
		JSONObject responseBody = new JSONObject();

		if (id == null) {
			responseBody.put("success", false);
			responseBody.put("message", "Id是必要欄位");
		} else {
			if (!orderMenuService.exists(id)) {
				responseBody.put("success", false);
				responseBody.put("message", "Id不存在");
			} else {
				OrderMenus product = orderMenuService.changeStatus(body);
				if (product == null) {
					responseBody.put("success", false);
					responseBody.put("message", "修改失敗");
				} else {
					responseBody.put("success", true);
					responseBody.put("message", "修改成功");
				}
			}
		}

		return responseBody.toString();
	}

	@PostMapping("/orderMenu/findbyname")
	public String findByName(@RequestBody String name) {
		JSONObject responseBody = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject(name);
		String itemName = obj.isNull("name") ? null : obj.getString("name");
		List<OrderMenus> bean = orderMenuService.findByNameLike(itemName);
		if (bean != null) {
			for(OrderMenus orderMenus : bean){
			String createtime = DatetimeConverter.toString(orderMenus.getCreateTime(), "yy-MM-dd");
			String updateTime = DatetimeConverter.toString(orderMenus.getUpdateTime(), "yy-MM-dd");
			JSONObject item;
			
				item = new JSONObject()
				.put("itemId", orderMenus.getItemId())
				.put("category", orderMenus.getCategory())
				.put("itemName", orderMenus.getItemName())
				.put("capacity", orderMenus.getCapacity())
				.put("price", orderMenus.getPrice())
				.put("status", orderMenus.getStatus())
				.put("creater", orderMenus.getCreateBy())
				.put("createtime", createtime)
				.put("updateBy", orderMenus.getUpdateBy())
				.put("updateTime", updateTime);
				array = array.put(item);
				responseBody.put("list", array);
			}
		}


		return responseBody.toString();
	}
}
