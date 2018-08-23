package com.duobi.manager.sys.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置session过期时间
 */
@Configuration
public class SessionTimeSet {

    //该值在配置文件中配置，且必须是60的整数倍，否则该值自动默认为60，即60S过期
    @Value("${maxSessionTimeoutInterval}")
    private int maxSessionTimeoutInterval;

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.setSessionTimeout(maxSessionTimeoutInterval);//单位为S
            }
        };
    }

}
