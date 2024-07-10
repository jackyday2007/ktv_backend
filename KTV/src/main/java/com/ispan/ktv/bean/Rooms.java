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
@Table(name = "rooms")
public class Rooms {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roomId")
	private Integer roomId;

	@Column(name = "size")
	private String size;

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

	// 與Orders 的 room 欄位
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private List<Orders> roomOrders = new ArrayList<>();

	// 與Problems 的 room 欄位
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private List<Problems> problems = new ArrayList<>();
}
