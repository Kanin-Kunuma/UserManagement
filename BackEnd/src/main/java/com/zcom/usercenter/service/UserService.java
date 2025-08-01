package com.zcom.usercenter.service;

import com.zcom.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param Password      用户密码
     * @param checkPassword 校验密码
     * @param planetCode    星球编号
     * @return 新用户id
     */
    long userRegister(String userAccount, String Password, String checkPassword, String planetCode);


    /**
     * 用户登录
     *
     * @param userAccount 用户账户
     * @param Password    用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String Password, HttpServletRequest request);


    /**
     * 将用户脱敏独立了出来
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);


    /**
     * 用户注销
     * @return
     */

    int userLogout(HttpServletRequest request);
}
