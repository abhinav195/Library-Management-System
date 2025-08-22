package com.library.issues.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    // propagate the JWT to downstream services
    @Bean
    public RequestInterceptor authRelay() {
        return template -> {
            var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                String header = attrs.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
                if (header != null) template.header(HttpHeaders.AUTHORIZATION, header);
            }
        };
    }
}
