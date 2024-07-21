package com.ispan.ktv.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "timeSlot")
public class TimeSlot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "timeSlotId")
	private Integer timeSlotId;
	
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startTime", columnDefinition = "time")
	private Date startTime;
	
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "endTime", columnDefinition = "time")
	private Date endTime;
	
	@Column(name = "date" , columnDefinition = "date")
	private Date date;
	
}
