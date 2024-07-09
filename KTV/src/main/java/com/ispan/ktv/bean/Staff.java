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
@Table(name="staff")
public class Staff {
	
	@Id
	@Column(name = "accountId")
	private Integer accountId;

	@Column(name = "account")
	private Integer account;

	@Column(name = "accountName")
	private String accountName;

	@Column(name = "password")
	private String password;
	
	@Column(name = "status")
	private Integer status;
	
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
	
	//與Problems 的 createBy 欄位 
	@OneToMany(mappedBy = "createBy" , cascade = CascadeType.ALL)
	private List<Problems> problemCreateBy = new ArrayList<>();
	
	//與Problems 的 updateBy 欄位 
	@OneToMany(mappedBy = "updateBy" , cascade = CascadeType.ALL)
	private List<Problems> problemUpdateBy = new ArrayList<>();
	
	//與News 的 createBy 欄位 
	@OneToMany(mappedBy = "createBy" , cascade = CascadeType.ALL)
	private List<News> newCreateBy = new ArrayList<>();

	//與News 的 updateBy 欄位 
	@OneToMany(mappedBy = "updateBy" , cascade = CascadeType.ALL)
	private List<News> newUpdateBy = new ArrayList<>();
	
}
