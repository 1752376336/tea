package com.tea.framework.autoconfigure;

import com.tea.framework.document.Swagger2Config;
import com.tea.framework.utils.ContextUtils;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration(classes = {Swagger2Config.class})
public class TeaServiceAutoConfiguration {


    @Bean
    public ContextUtils contextUtils() {
        return new ContextUtils();
    }

}
