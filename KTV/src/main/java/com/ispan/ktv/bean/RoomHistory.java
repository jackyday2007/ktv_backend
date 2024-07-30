package com.ispan.ktv.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="roomHistory")
@Setter
@Getter
@NoArgsConstructor
public class RoomHistory {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@JoinColumn(name="room")
	@ManyToOne
	private Rooms room;
	
	@Column(name="date", columnDefinition = "date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	@Column(name="startTime", columnDefinition = "time")
	private Date startTime;
	
	@Column(name="endTime", columnDefinition = "time")
	private Date endTime;
	
	@Column(name="status")
	private String status;
	
	@Column(name="createTime", columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@PrePersist
	public void onCreate() {
	      if (createTime == null) {
	          createTime = new Date();
	      }
	  }

}
