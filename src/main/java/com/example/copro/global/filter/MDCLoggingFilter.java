package com.example.copro.global.filter;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        MDC.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
        chain.doFilter(request, response);
        MDC.clear();
    }
}
