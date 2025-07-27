package com.zcom.usercenter.model.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 */

// 用lombok的Data注解, 生成getter和setter方法
@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = -3134065258610678122L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
