package com.github.dapeng.dms.web.config;

/**
 * @author Denim.leihz 2020-01-05 2:59 PM
 */

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 用于编码密码的服务接口的实现类。
 */
@Component
public class MyPasswordEncoder implements PasswordEncoder {

    /**
     * 编码原始密码。通常，良好的编码算法应用SHA-1或更大的哈希与8字节或更大的随机生成的盐相结合。
     *
     * @param rawPassword 密码，一个可读的字符值序列
     * @return
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    /**
     * 验证从存储中获得的编码密码是否与提交的原始密码匹配。如果密码匹配，返回true;如果不匹配，返回false。存储的密码本身永远不会被解码。
     *
     * @param rawPassword     预设的验证密码。要编码和匹配的原始密码
     * @param encodedPassword 表单输入的密码。来自存储的编码密码与之比较
     * @return
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(rawPassword.toString());
    }
}
