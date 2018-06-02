package com.exam.conf;

import com.exam.interceptor.PageInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by deng on 2017/10/19.
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对student页面和teacher页面做权限控制
        registry.addInterceptor(new PageInterceptor()).addPathPatterns("/student/**").addPathPatterns("/teacher/**");
        super.addInterceptors(registry);
    }

}
