package com.kangyonggan.gitlab.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @author kyg
 */
public interface AppConstants {

    /**
     * 默认编码
     */
    String DEFAULT_CHARSET = "UTF-8";

    /**
     * Hash Interations
     */
    int HASH_INTERATIONS = 2;

    /**
     * Salt Size
     */
    int SALT_SIZE = 8;

    /**
     * token在header中的名称
     */
    String HEADER_TOKEN_NAME = "x-auth-token";

    /**
     * 用户在session中的key
     */
    String KEY_SESSION_USER = "key-session-user";

    /**
     * 保留字
     */
    List<String> RESERVED_WORDS = Arrays.asList("admin", "users", "profile", "404");

}
