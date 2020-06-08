package com.kangyonggan.gitlab.configuration;

import com.kangyonggan.gitlab.constants.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author kyg
 */
@Configuration
public class MvcConfigure implements WebMvcConfigurer {

    /**
     * 文件跟路径
     */
    @Value("${app.file-upload}")
    private String fileUploadPath;

    /**
     * 允许跨域
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS").exposedHeaders(AppConstants.HEADER_TOKEN_NAME);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    /**
     * 处理上传文件的路径
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + fileUploadPath);
    }
}
