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
@Table(name="members")
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Integer memberId;
    
    @Column(name = "idNumber", length = 10)
    private String idNumber;
    
    @Column(name = "password", columnDefinition = "nvarchar(255)")
    private String password;
    
    @Column(name = "memberName", columnDefinition = "nvarchar(255)")
    private String memberName;
    
    @Column(name = "phone", length = 15)
    private String phone;
    
    @Column(name = "birth", columnDefinition = "date")
    private Date birth;
    
    @Column(name = "email", columnDefinition = "nvarchar(255)")
    private String email;
    
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
    
    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL)
    private List<Orders> memberOrders = new ArrayList<>();
}
