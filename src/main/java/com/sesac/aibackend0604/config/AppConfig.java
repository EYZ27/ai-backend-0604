package com.sesac.aibackend0604.config;

import com.sesac.aibackend0604.util.MessageFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MessageFormatter messageFormatter() {
        return new MessageFormatter();
    }
}

