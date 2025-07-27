package com.zcom.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcom.usercenter.common.ErrorCode;
import com.zcom.usercenter.exception.BusinessException;
import com.zcom.usercenter.service.UserService;
import com.zcom.usercenter.model.domain.User;
import com.zcom.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zcom.usercenter.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 加盐值, 来混淆密码
     */
    private static final String SALT = "yupi";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {

        // 1. 校验与检验
        // 校验三个参数是否为非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            // todo 修改为自定义异常
            /*return -1;*/
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        // 校验账户长度
        if (userAccount.length() < 4) {
            /*return -1;*/
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }

        // 校验密码长度
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            /*return -1;*/
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }

        // 校验星球编号长度
        if (planetCode.length() > 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号过长");
        }



        // 校验账户是否包含特殊字符（使用正则表达式）
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        // 若账户包含特殊字符, 则直接返回, 不对数据库进行操作
        if (matcher.find()) {
            return -1;
        }

        // 密码和校验密码是否相同
        // 不能用等于去判断两个字符串, 要用equals方法
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        // 检验是否有重复账户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }

        // 检验是否有重复星球编号
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        // 1. 校验与检验
        // 校验三个参数是否为非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        // 校验账户长度
        if (userAccount.length() < 4) {
            return null;
        }

        // 校验密码长度
        if (userPassword.length() < 8) {
            return null;
        }

        // 校验账户是否包含特殊字符（使用正则表达式）
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);

        // 若账户包含特殊字符, 则直接返回, 不对数据库进行操作
        if (matcher.find()) {
            return null;
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);

        // 用户不存在
        if (user == null) {
            // 用英文不容易出乱码
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }

        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);

        // 4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;

    }


    /**
     * 将用户脱敏方法独立出来
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {

        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }



    @Override
    public int userLogout(HttpServletRequest request) {

        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


}













