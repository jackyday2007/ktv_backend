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
@Table(name = "problems")
public class Problems {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "problemId")
	private Integer problemId;

	@Column(name = "eventCase")
	private String eventCase;
	
	//此為多方 與 Rooms 的 roomId 欄位
	@ManyToOne
	@JoinColumn(name = "room")
	private Rooms room;
		
	@Column(name = "content" , columnDefinition = "nvarchar(255)")
	private String content;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "eventDate")
	private Date eventDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "closeDate")
	private Date closeDate;
	
	@Column(name = "status")
	private String status;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime")
	private Date createTime;
	
	//此為多方 與 Staff 的 AccountId 欄位
	@ManyToOne
    @JoinColumn(name = "createBy")
    private Staff createBy;

	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime")
	private Date updateTime;

	//此為多方 與 Staff 的 updateBy 欄位
	@ManyToOne
    @JoinColumn(name = "updateBy")
	private Staff updateBy;
	
	@PrePersist
	public void onCreate() {
		if (createTime == null) {
			createTime = new Date();
		}
		updateRoomStatus();
	}
	
	@PreUpdate
	public void onUpdate() {
	    if (room != null) {
	        if ("處理中".equals(this.status)) {
	            room.setStatus("維護中");
	        } else if ("結案".equals(this.status)) {
	            room.setStatus("可使用");
	        }
	        // 確保房間狀態被更新
	        room.setStatus(room.getStatus()); // Save the updated room status
	    }
	    // 確保 updateTime 被更新
	    this.updateTime = new Date();
	}
	
	
    private void updateRoomStatus() {
        if ("處理中".equals(this.status)) {
            room.setStatus("維護中");
        } else if ("結案".equals(this.status)) {
            room.setStatus("可使用");
        }
    }
	
}
