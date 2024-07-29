package com.ispan.ktv.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
	@Column(name = "roomId")
	private Integer roomId;

	@Column(name = "size")
	private String size;

	@Column(name = "price")
	private Double price;

	@Column(name = "status")
	private String status;

	@Lob
	@Column(name = "photoFile")
	private byte[] photoFile;

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

	@PreUpdate
    public void onUpdate() {
        if (!"維護中".equals(this.status)) {
            this.updateTime = new Date();
        }
    }

	// 與Orders 的 room 欄位
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Orders> roomOrders = new ArrayList<>();

	// 與Problems 的 room 欄位
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Problems> problems = new ArrayList<>();
	
	// 與RoomHistory 的 room 欄位
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<RoomHistory> roomHistory = new ArrayList<>();
		
	// 檢查房間是否處於維護中
    public boolean isUnderMaintenance() {
        return "維護中".equals(this.status);
    }

    // 設定房間狀態方法，若為維護中則禁止修改
    public void setStatus(String status) {
        if ("處理中".equals(status)) {
            this.status = "維護中";
        } else if ("結案".equals(status)) {
            this.status = "可使用";
        } else {
            this.status = status;
        }
    }


    public void updateRoomStatus(String problemStatus) {
        if ("處理中".equals(problemStatus)) {
            this.status = "維護中";
        } else if ("結案".equals(problemStatus)) {
            this.status = "可使用";
        }
    }

}
