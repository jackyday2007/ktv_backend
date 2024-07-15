package com.ispan.ktv.controller;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    private String idNumber;  // 會員ID號碼
    private String email;  // 會員電子郵件

}

