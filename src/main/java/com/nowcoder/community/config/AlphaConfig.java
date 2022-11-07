package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

// 普通配置类
@Configuration
public class AlphaConfig {

    // 装配 java 中已有的类
    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    }
}
