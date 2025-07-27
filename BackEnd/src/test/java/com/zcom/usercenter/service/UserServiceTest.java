package com.zcom.usercenter.service;
import java.util.Date;

import com.zcom.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * 用户服务测试
 */

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    // 用户注册测试
    @Test
    public void testAddUser() {

        // 若报错, 则是因为MyBatis-Plus的开启了自动驼峰命名规则, 需要到yml文件中手动关闭
        User user = new User();
        user.setId(0L);
        user.setUsername("人机钉巴");
        user.setUserAccount("88485678");
        user.setAvatarUrl("https://q1.qlogo.cn/g?b=qq&nk=1437996009&s=160");
        user.setGender(0);
        user.setUserPassword("12345678");
        user.setPhone("10086");
        user.setEmail("srjei30@bitch.com");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        // 调用save方法, 往数据库中插入一条数据
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }


    // 数据库表单元测试
    @Test
    void userRegister() {

        // 校验密码是否为空
        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 校验账户长度是否符合要求
        userAccount = "yu";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);


        userAccount = "yupi";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 校验账户是否有特殊字符
        userAccount = "yu pi";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 校验密码是否与二次确认密码相同
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 校验是否有重复账户
        userAccount = "dogYupi";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 正常插入
        userAccount = "yupi";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(1, result);
    }
}