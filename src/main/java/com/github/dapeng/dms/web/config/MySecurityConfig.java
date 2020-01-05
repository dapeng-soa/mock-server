package com.github.dapeng.dms.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Denim.leihz 2020-01-05 2:47 PM
 */
@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.httpBasic()
                .and()
                // 身份认证
                .authorizeRequests()
                // 所有请求
                .anyRequest()
                // 身份认证
                .authenticated();*/


        http
                .csrf().disable()
                .cors().disable()
                // 表单认证
                .formLogin()
                // 登录页
//                .loginPage("/login")
                // 登录表单提交地址
                .loginProcessingUrl("/auth/login")
                .and()
                // 身份认证请求
                .authorizeRequests()
                // URL路径匹配
                .antMatchers("/login").permitAll()
                .antMatchers("/out/**").permitAll()
                // 任意请求
                .anyRequest()
                // 身份认证
                .authenticated().and().logout().logoutUrl("/logout");

    }

}