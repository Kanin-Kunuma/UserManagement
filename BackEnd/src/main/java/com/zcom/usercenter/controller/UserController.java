package com.zcom.usercenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcom.usercenter.common.BaseResponse;
import com.zcom.usercenter.common.ErrorCode;
import com.zcom.usercenter.common.ResultUtils;
import com.zcom.usercenter.exception.BusinessException;
import com.zcom.usercenter.model.domain.User;
import com.zcom.usercenter.model.request.UserLoginRequest;
import com.zcom.usercenter.model.request.UserRegisterRequest;
import com.zcom.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.util.Password;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zcom.usercenter.constant.UserConstant.ADMIT_ROLE;
import static com.zcom.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 */
@RestController // 此注解
@RequestMapping("/user")
/*@CrossOrigin(origins = { "http://101.33.253.157" }, allowCredentials = "true")*/
public class UserController{

    @Resource
    private UserService userService;

    @PostMapping("/register")
    // 让前端传来json的请求参数
    // @RequestBody注解， SpringMVC把前端传来的json参数去和这个 UserRegisterRequest userRegisterRequest 做一个关联
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest == null) {
            /*return ResultUtils.error(ErrorCode.PARAMS_ERROR);*/
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // Q：为什么还要在controller再校验一次?
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();


        // 如果上面三个参数有任何一个为空, 不进入业务逻辑层, 直接在此返回空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        long result =  userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }


    @PostMapping("/login")
    // 让前端传来json的请求参数
    // @RequestBody注解， SpringMVC把前端传来的json参数去和这个 UserRegisterRequest userRegisterRequest 做一个关联
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if (userLoginRequest == null) {
            /*return null*/;
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 如果上面两个参数有任何一个为空, 不进入业务逻辑层, 直接在此返回空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }


    @PostMapping("/logout")
    // 让前端传来json的请求参数
    // @RequestBody注解， SpringMVC把前端传来的json参数去和这个 UserRegisterRequest userRegisterRequest 做一个关联
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {

        if (request == null) {
            return null;
        }

        int result =  userService.userLogout(request);
        return ResultUtils.success(result);
    }


    @GetMapping("/currentUser")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {

        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            /*return null;*/
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // todo 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser =  userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }


    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {

        // 仅管理员可查询
        // Q：为什么getAttribute里不用常量？
        // A：本系统处理的实体只有User一个
        /*        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getRole() != ADMIT_ROLE) {
            // 用户角色不为管理员, 返回一个空数组
            return new ArrayList<>();
        }*/
        if (!isAdmin(request)) {
            // 不为管理员角色, 则返回一个空列表
            /*return new ArrayList<>();*/
            return null;
        }

        QueryWrapper<User> queryWrapper= new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        /*return userService.list(queryWrapper);*/
        // Java8的内容
        List<User> userList = userService.list(queryWrapper);
        List<User> list =  userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {

        if (!isAdmin(request)) {
            return null;
        }

        if (id <= 0) {
            return null;
        }

        boolean result =  userService.removeById(id);
        return ResultUtils.success(result);
    }


    // 为避免要写多段重复代码
    // 需要编写公有方法
    /**
     * 判断是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {

        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIT_ROLE;
    }
 }
