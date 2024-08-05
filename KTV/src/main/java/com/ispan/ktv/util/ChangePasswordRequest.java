package com.ispan.ktv.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String idNumber;
    private String oldPassword;
    private String newPassword;
}

