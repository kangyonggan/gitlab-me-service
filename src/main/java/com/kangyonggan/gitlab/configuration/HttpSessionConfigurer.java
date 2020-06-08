package com.kangyonggan.gitlab.configuration;

import com.kangyonggan.gitlab.constants.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

/**
 * @author kyg
 */
@Configuration
@EnableRedisHttpSession(redisNamespace = "gitlab:session")
public class HttpSessionConfigurer {

    /**
     * token放在http请求的header中，name=x-auth-token
     *
     * @return
     */
    @Bean
    public HeaderHttpSessionIdResolver httpSessionStrategy() {
        return new HeaderHttpSessionIdResolver(AppConstants.HEADER_TOKEN_NAME);
    }

}
