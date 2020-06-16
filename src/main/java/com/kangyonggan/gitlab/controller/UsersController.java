package com.kangyonggan.gitlab.controller;

import com.alibaba.fastjson.JSONObject;
import com.kangyonggan.gitlab.annotation.PermissionLogin;
import com.kangyonggan.gitlab.constants.AccessLevel;
import com.kangyonggan.gitlab.constants.AppConstants;
import com.kangyonggan.gitlab.constants.YesNo;
import com.kangyonggan.gitlab.dto.Response;
import com.kangyonggan.gitlab.interceptor.ParamsInterceptor;
import com.kangyonggan.gitlab.model.Email;
import com.kangyonggan.gitlab.model.User;
import com.kangyonggan.gitlab.service.EmailService;
import com.kangyonggan.gitlab.service.SignInLogService;
import com.kangyonggan.gitlab.service.UserService;
import com.kangyonggan.gitlab.util.DateUtil;
import com.kangyonggan.gitlab.util.Digests;
import com.kangyonggan.gitlab.util.Encodes;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author kyg
 */
@RestController
@RequestMapping("users")
@Log4j2
public class UsersController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private SignInLogService saveAccessLog;

    @Autowired
    private EmailService emailService;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("signUp")
    public Response signUp(User user) {
        user.setSignUpIp(getIpAddress());
        user.setProjectsLimit(0);
        user.setCanCreateGroup(YesNo.NO.getCode());
        user.setAccessLevel(AccessLevel.Regular.name());
        user.setAvatar(null);
        userService.saveUser(user);

        return successResponse();
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param rememberMe
     * @return
     */
    @PostMapping("signIn")
    public Response signIn(@RequestParam String username, @RequestParam String password,
                           @RequestParam(required = false, defaultValue = "0") int rememberMe) {
        Response response = successResponse();

        User dbUser = userService.findUserByUsernameOrEmail(username);
        if (dbUser == null) {
            log.warn("用户名/邮箱不存在:{}", username);
            return response.failure("The username does not exist");
        }
        if (dbUser.getIsDeleted().equals(YesNo.YES.getCode())) {
            return response.failure("8002", "The account is disabled");
        }

        byte[] salt = Encodes.decodeHex(dbUser.getSalt());
        byte[] hashPassword = Digests.sha1(password.getBytes(), salt, AppConstants.HASH_INTERATIONS);
        String realPassword = Encodes.encodeHex(hashPassword);
        if (!dbUser.getPassword().equals(realPassword)) {
            log.warn("密码错误, username:{}", username);
            return response.failure("Incorrect username or password");
        }

        // 把登录信息放入session
        HttpSession session = ParamsInterceptor.getSession();
        session.setAttribute(AppConstants.KEY_SESSION_USER, dbUser);
        log.info("登录成功,sessionId:{}", session.getId());

        // 更新最后登录时间
        dbUser.setLastSignInIp(getIpAddress());
        dbUser.setLastSignInTime(new Date());
        userService.updateUser(dbUser);

        // 保存登录日志
        saveAccessLog.saveSignInLog(dbUser.getId(), getIpAddress());

        // Remember me
        if (rememberMe == 1) {
            // 77776000秒 = 90天
            ParamsInterceptor.getSession().setMaxInactiveInterval(77776000);
        }

        response.put("user", dbUser);
        return response;
    }

    /**
     * 登出
     *
     * @param session
     * @return
     */
    @GetMapping("signOut")
    @PermissionLogin
    public Response signOut(HttpSession session) {
        log.info("登出成功,sessionId:{}", session.getId());
        session.invalidate();
        return successResponse();
    }

    /**
     * 发重置密码邮件
     *
     * @param email
     * @return
     * @throws Exception
     */
    @PostMapping("sendResetPasswordEmail")
    public Response sendResetPasswordEmail(@RequestParam String email) throws Exception {
        Response response = successResponse();

        response.put("emailId", userService.sendResetPasswordEmail(email));
        return response;
    }

    /**
     * 重置密码
     *
     * @param emailId
     * @param verifyCode
     * @param password
     * @return
     */
    @PutMapping("resetPassword")
    public Response resetPassword(@RequestParam Long emailId, @RequestParam String verifyCode, @RequestParam String password) {
        Response response = successResponse();
        Email email = emailService.getEmail(emailId);
        if (email == null || email.getIsDeleted() == YesNo.YES.getCode()) {
            return response.failure("Invalid verification code");
        }
        String expiresIn = JSONObject.parseObject(email.getParams()).getString("expiresIn");
        if (new Date().after(DateUtil.plusMinutes(email.getCreatedTime(), Integer.parseInt(expiresIn)))) {
            return response.failure("Verification code was expired");
        }
        userService.resetPassword(email.getToEmail(), password);
        emailService.deleteEmail(emailId);
        return response;
    }
}
