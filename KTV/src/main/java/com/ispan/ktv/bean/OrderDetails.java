package com.ispan.ktv.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "orderDetails")
public class OrderDetails {

	@Id
	@Column(name = "orderDetailId")
	private Integer orderDetailId;

	// 此為多方 與 Orders 的 orderId 欄位
	@ManyToOne
	@JoinColumn(name = "orderId")
	private Orders orderId;

	@Column(name = "item")
	private String item;

	@Column(name = "quantity")
	private Integer quantity;

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

}
