package com.konnector.backendapi.security;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SessionCookieFilter extends GenericFilterBean {

    private final String SESSION_COOKIE_NAME = "JSESSIONID";
//    private final String SAME_SITE_ATTRIBUTE_VALUES = ";HttpOnly;Secure;SameSite=Strict";
    private final String SAME_SITE_ATTRIBUTE_VALUES = ";SameSite=None";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String requestUrl = httpServletRequest.getRequestURL().toString();

//        Cookie[] cookies = httpServletRequest.getCookies(); // check can we access the coyote (tomcat?) request/response cookies here?
//        if (cookies != null && cookies.length > 0) {
//            List<Cookie> cookieList = Arrays.asList(cookies);
//            Cookie sessionCookie = cookieList.stream().filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName())).findFirst().orElse(null);
//            if (sessionCookie != null) {
////                sessionCookie.setValue(sessionCookie.getValue() + SAME_SITE_ATTRIBUTE_VALUES);
//                httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, sessionCookie.getName() + "=" + sessionCookie.getValue() + SAME_SITE_ATTRIBUTE_VALUES); // was this adding a NEW jsessionid?
//                Cookie[] cookies2 = httpServletRequest.getCookies();
//            }
//        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
