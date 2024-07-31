package com.ispan.ktv.util;
//import org.springframework.stereotype.Component;
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//@Component
//public class JsonWebTokenFilter implements Filter {
//
//    private final JsonWebTokenUtility jsonWebTokenUtility;
//
//    public JsonWebTokenFilter(JsonWebTokenUtility jsonWebTokenUtility) {
//        this.jsonWebTokenUtility = jsonWebTokenUtility;
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // Initialization logic if needed
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String requestURI = httpRequest.getRequestURI();
//        String method = httpRequest.getMethod();
//
//        // Skip JWT validation for /api/login, /api/register, and OPTIONS requests
//        if ("/api/login".equals(requestURI) || "/api/register".equals(requestURI) || "OPTIONS".equalsIgnoreCase(method)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // Process JWT for other requests
//        String auth = httpRequest.getHeader("Authorization");
//        if (auth != null && auth.startsWith("Bearer ")) {
//            String token = auth.substring(7); // Remove "Bearer " prefix
//            String result = jsonWebTokenUtility.validateToken(token);
//            if (result != null) {
//                // Token is valid, continue processing the request
//                chain.doFilter(request, response);
//                return;
//            }
//        }
//
//        // Invalid or missing JWT, respond with 403 Forbidden
//        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
//        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
//    }
//
//    @Override
//    public void destroy() {
//        // Cleanup logic if needed
//    }
//}
