package com.github.dapeng.dms.web.config;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Denim.leihz 2020-01-05 2:54 PM
 */
@Component
public class MyUserDetailsSerice implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return new User(name, "youjie1314", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
