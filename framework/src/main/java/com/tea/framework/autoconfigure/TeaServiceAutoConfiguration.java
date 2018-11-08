package com.tea.framework.autoconfigure;

import com.tea.framework.document.Swagger2Config;
import com.tea.framework.utils.ContextUtils;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@ImportAutoConfiguration(classes = {Swagger2Config.class})
@EnableResourceServer
public class TeaServiceAutoConfiguration {


    @Bean
    public ContextUtils contextUtils() {
        return new ContextUtils();
    }

}
