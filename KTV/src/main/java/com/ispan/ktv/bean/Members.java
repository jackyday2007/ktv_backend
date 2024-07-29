package com.ispan.ktv.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
@Table(name="members")
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Integer memberId;  // 會員ID
    
    @Column(name = "idNumber", length = 10)
    private String idNumber;  // 帳號
    
    @Column(name = "password", columnDefinition = "nvarchar(255)")
    private String password;  // 密碼
    
    @Column(name = "memberName", columnDefinition = "nvarchar(255)")
    private String memberName;  // 姓名
    
    @Column(name = "phone", length = 15)
    private String phone;  // 會員電話
    
    @Column(name = "birth", columnDefinition = "date")
    private Date birth;  // 會員出生日期
    
    @Column(name = "email", columnDefinition = "nvarchar(255)")
    private String email;  // 會員電子郵件
    
    @Column(name = "status")
    private Integer status;  // 會員狀態
    
    @Lob
	@Column(name="image")
	private byte[] image; //圖片
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime")
    private Date createTime;  // 創建時間
    
    @Column(name = "createBy")
    private String createBy;  // 創建者
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updateTime")
    private Date updateTime;  // 更新時間
    
    @Column(name = "updateBy")
    private String updateBy;  // 更新者
    
    @Column(name = "resetPasswordToken")
    private String resetPasswordToken;  // 用來儲存重設密碼的 token
    
    @Column(name = "resetPasswordTokenExpiry")
    private Date resetPasswordTokenExpiry;  // 用來儲存 token 的過期時間
    
    @PrePersist
    public void onCreate() {
        // 在新實體被儲存到資料庫之前，設定 createTime 為當前時間
        if (createTime == null) {
            createTime = new Date();
        }
    }
    
    @JsonIgnore
    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL)
    private List<Orders> memberOrders = new ArrayList<>();  // 會員的訂單列表
}
