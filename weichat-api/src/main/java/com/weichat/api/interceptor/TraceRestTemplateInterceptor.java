package com.weichat.api.interceptor;

import com.weichat.api.filter.TraceFilter;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * RestTemplate 拦截器 — 从 MDC 获取 traceId 并注入到出站 HTTP 请求 header，
 * 实现跨服务链路追踪的自动传播。
 */
public class TraceRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                         ClientHttpRequestExecution execution) throws IOException {
        String traceId = MDC.get(TraceFilter.MDC_KEY);
        if (traceId != null && !traceId.isEmpty()) {
            request.getHeaders().add(TraceFilter.TRACE_HEADER, traceId);
        }
        return execution.execute(request, body);
    }
}