package com.github.dapeng.dms.web.util;

import com.github.dapeng.dms.web.controller.AdminController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-08 2:13 PM
 */
@Component
@Slf4j
public class DestroyPostProcessor implements DestructionAwareBeanPostProcessor {
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        log.info("Spring Container destroyed, access beanName: " + beanName);
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return true;
    }
}
