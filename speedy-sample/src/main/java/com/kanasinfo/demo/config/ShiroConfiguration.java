package com.kanasinfo.demo.config;

import com.kanasinfo.demo.service.UserService;
import com.kanasinfo.demo.model.User;
import com.kanasinfo.ext.shiro.ShiroExtConfiguration;
import com.kanasinfo.ext.shiro.ShiroExtRealm;
import com.kanasinfo.ext.shiro.bean.ShiroExtConfig;
import com.kanasinfo.ext.shiro.bean.ShiroUser;
import com.kanasinfo.ext.shiro.bean.UserModel;
import com.kanasinfo.ext.shiro.filter.ShiroExtFormAuthenticationFilter;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by gefangshuai on 2016/1/5.
 */
@Configuration
public class ShiroConfiguration extends ShiroExtConfiguration implements ApplicationContextAware{
    private Logger logger = Logger.getLogger(ShiroConfiguration.class);
    private ApplicationContext context;

    @Override
    protected ShiroExtConfig shiroExtConfig() {
        return new ShiroExtConfig() {
            @Override
            public ShiroExtRealm getShiroExtRealm() {
                return new ShiroExtRealm() {
                    @Override
                    protected UserModel getUserByUsername(String username) {
                        UserService userService = (UserService) context.getBean("userService");
                        User user = userService.findByUsername(username);
                        return user;
                    }

                    @Override
                    protected Set<String> getRolesByUsername(String username) {
                        return null;
                    }

                    @Override
                    protected Set<String> getPermissionsByUsername(String username) {
                        return null;
                    }
                };
            }

            @Override
            public ShiroExtFormAuthenticationFilter getShiroServerFormAuthenticationFilter() {
                return new ShiroExtFormAuthenticationFilter() {
                    @Override
                    protected void doSomethingOnLoginSuccess(ApplicationContext context, AuthenticationToken token, ShiroUser shiroUser, Subject subject, ServletRequest request, ServletResponse response) {
                        logger.debug("login success!");
                    }
                };
            }

            @Override
            public String getLoginUrl() {
                return "/login";
            }

            @Override
            public String getSuccessUrl() {
                return "/";
            }

            @Override
            public String getUnauthorizedUrl() {
                return "/unauthorized";
            }

            @Override
            public Map<String, String> getFilterChainDefinitionMap() {
                Map<String, String> filterMap = new LinkedHashMap<>();
                filterMap.put("/favicon.ico", "anon");
                filterMap.put("/static/**", "anon");
                filterMap.put("/u/**", "anon");
                filterMap.put("/p/**", "authc");
                filterMap.put("/login", "authc");
                filterMap.put("/**", "anon");
//                filterMap.put("/**", "authc");
                return filterMap;
            }
        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
