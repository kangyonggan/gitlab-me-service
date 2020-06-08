package com.kangyonggan.gitlab.constants;

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
}
