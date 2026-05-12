package com.weichat.api.config;

import com.weichat.api.interceptor.TraceRestTemplateInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${rest.template.connect-timeout-ms:30000}")
    private int connectTimeoutMs;

    @Value("${rest.template.read-timeout-ms:120000}")
    private int readTimeoutMs;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeoutMs);
        factory.setReadTimeout(readTimeoutMs);
        RestTemplate rt = new RestTemplate(factory);
        // 添加 traceId 传播拦截器 — 所有出站 HTTP 请求自动携带 X-Trace-Id
        rt.getInterceptors().add(new TraceRestTemplateInterceptor());
        return rt;
    }
}