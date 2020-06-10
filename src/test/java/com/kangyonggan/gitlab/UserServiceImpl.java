package com.kangyonggan.gitlab;

import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kyg
 */
public class UserServiceImpl extends AbstractTest {

    @Autowired
    private UserService userService;

    @Test
    public void saveUser() {
        User user = new User();

        user.setFullName("康永敢");
        user.setUsername("kangyonggan");
        user.setPassword("11111111");
        user.setEmail("java@kangyonggan.com");

        user.setSignUpIp("127.0.0.1");

        userService.saveUser(user);
    }

    @Test
    public void sendResetPasswordEmail() throws Exception {
        userService.sendResetPasswordEmail("java@kangyonggan.com");
    }

}
