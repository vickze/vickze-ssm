package io.vickze.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.vickze.shiro.cache.RedisCacheManager;
import io.vickze.shiro.cache.RedisSessionDAO;
import io.vickze.shiro.realm.AuthRealm;
import redis.clients.jedis.JedisPool;

/**
 * Shiro配置
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-08 22:07
 */
@Configuration
public class ShiroConfig {

    @Bean("rememberMeCookie")
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setHttpOnly(true);
        //过期时间7天
        simpleCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7L));
        simpleCookie.setName("RSESSIONID");

        return simpleCookie;
    }

    @Bean("rememberMeManager")
    public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie);
        return cookieRememberMeManager;
    }


    @Bean("sessionManager")
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        //不需定时删除session，在RedisSessionDAO设置过期时间即可
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionDAO(redisSessionDAO);
        //session过期时间两小时
        sessionManager.setGlobalSessionTimeout(2 * 60 * 60 * 1000);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(AuthRealm authRealm, SessionManager sessionManager,
                                           CookieRememberMeManager rememberMeManager, RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(authRealm);
        securityManager.setCacheManager(redisCacheManager);
        securityManager.setRememberMeManager(rememberMeManager);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/", "anon");
        filterMap.put("/sys/**", "user");
        //filterMap.put("/**", "authc");

        shiroFilter.setLoginUrl("/unauthorized");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.setEnabled(true);
        registration.setOrder(Integer.MAX_VALUE - 1);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
