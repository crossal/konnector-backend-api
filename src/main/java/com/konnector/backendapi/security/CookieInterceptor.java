package com.konnector.backendapi.security;

//@Component
//public class CookieInterceptor implements ApplicationListener<ContextRefreshedEvent> {

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.stereotype.Component;

@Component
public class CookieInterceptor {

//    private final ApplicationContext applicationContext;

//    public CookieInterceptor(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }

//    public CookieInterceptor() {
//        super();
//    }

//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        DefaultCookieSerializer cookieSerializer = contextRefreshedEvent.getApplicationContext().getBean(DefaultCookieSerializer.class);
////        log.info("Received DefaultCookieSerializer, Overriding SameSite Strict");
//        cookieSerializer.setUseHttpOnlyCookie(true);
//        cookieSerializer.setUseSecureCookie(true);
//        cookieSerializer.setSameSite("strict");
//    }

//    @EventListener
//    public void onEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        DefaultCookieSerializer cookieSerializer = contextRefreshedEvent.getApplicationContext().getBean(DefaultCookieSerializer.class);
////        log.info("Received DefaultCookieSerializer, Overriding SameSite Strict");
//        cookieSerializer.setUseHttpOnlyCookie(true);
//        cookieSerializer.setUseSecureCookie(true);
//        cookieSerializer.setSameSite("strict");
//    }
}
