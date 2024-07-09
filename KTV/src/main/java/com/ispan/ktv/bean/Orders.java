package com.ispan.ktv.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="orders")
public class Orders {
	
	@Id
	@Column(name = "orderId")
	private Integer orderId; 
	
	
	//此為多方 與 Customers 的 customerId 欄位
	@ManyToOne
	@JoinColumn(name = "customerId")
	private Customers customerId;
	
	//此為多方 與 Members 的 memberId 欄位
	@ManyToOne
	@JoinColumn(name = "memberId")
	private Members memberId;
	
	//此為多方 與 Rooms 的 roomNumber 欄位
	@ManyToOne
	@JoinColumn(name = "room")
	private Rooms room;
	
	@Column(name = "numberOfPersons")
	private Integer numberOfPersons;
	
	@Column(name = "hours")
	private Integer hours;
	
	@Column(name = "orderDate" , columnDefinition = "date")
	private Date orderDate;
	
	@Column(name = "startTime" , columnDefinition = "time")
	private Date startTime;
	
	@Column(name = "endTime" , columnDefinition = "time")
	private Date endTime;
	
	@Column(name = "subTotal")
	private Double subTotal;
	
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
	
	//與OrderDetails 的 orderId 欄位 
	@OneToMany(mappedBy = "orderId" , cascade = CascadeType.ALL)
	private List<OrderDetails> orderDetails = new ArrayList<>();
	
	//與OrdersStatusHistory 的 orderId 欄位 
	@OneToMany(mappedBy = "orderId" , cascade = CascadeType.ALL)
	private List<OrdersStatusHistory> ordersStatusHistory = new ArrayList<>();
}
