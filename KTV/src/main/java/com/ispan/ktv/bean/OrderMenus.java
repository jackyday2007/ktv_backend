package com.ispan.ktv.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "orderMenus")
public class OrderMenus {

	@Id
	@Column(name = "itemId")
	private String itemId;

	@Column(name = "itemName", columnDefinition = "nvarchar(255)")
	private String itemName;

	@Column(name = "category")
	private String category;

	@Column(name = "price")
	private Double price;

	@Column(name = "status")
	private String status;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime")
	private Date createTime;

	@Column(name = "createBy")
	private String createBy;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime")
	private Date updateTime;

	@Column(name = "updateBy")
	private String updateBy;

	@PrePersist
	public void onCreate() {
		if (createTime == null) {
			createTime = new Date();
		}
	}

	// 與OrderDetails 的 item 欄位
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	private List<OrderDetails> orderDetails = new ArrayList<>();

}
