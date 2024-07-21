package com.ispan.ktv.bean;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "customers")
public class Customers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customerId")
	private Integer customerId;
	
	@Column(name = "idNumber")
	private String idNumber;
	
	@Column(name = "name", columnDefinition = "nvarchar(255)")
	private String name;
	
	@Column(name = "phone")
	private Integer phone;
	
	@Column(name = "birth" , columnDefinition = "date")
	private Date birth;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime" )
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
	
	//與Orders 的 customerId 欄位 
	@OneToMany(mappedBy = "customerId" , cascade = CascadeType.ALL)
	private List<Orders> customerOrders = new ArrayList<>();
	
}
