package com.ispan.ktv.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "news")
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "newsId")
	private Integer newsId;

	@Column(name = "title")
	private String title;

	@Column(name = "url")
	private String url;

	@Lob
	@Column(name = "image")
	private byte[] image;

	@Column(name = "content", columnDefinition = "nvarchar(255)")
	private String content;

	@Column(name = "activityStartDate", columnDefinition = "date")
	private Date activityStartDate;

	@Column(name = "startDate", columnDefinition = "date")
	private Date startDate;

	@Column(name = "endDate", columnDefinition = "date")
	private Date endDate;

	@Column(name = "status")
	private String status;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime")
	private Date createTime;

	// 此為多方 與 Staff 的 AccountId 欄位
	@ManyToOne
	@JoinColumn(name = "createBy")
	private Staff createBy;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime")
	private Date updateTime;

	// 此為多方 與 Staff 的 AccountId 欄位
	@ManyToOne
	@JoinColumn(name = "updateBy")
	private Staff updateBy;

	@PrePersist
	public void onCreate() {
		if (createTime == null) {
			createTime = new Date();
		}
	}
}
