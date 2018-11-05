package com.github.dapeng.dms.mock.metadata;

import com.github.dapeng.dms.util.SysEnvUtil;
import com.github.dapeng.openapi.cache.ZkBootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * desc: zookeeper host will set in the environment
 *
 * @author hz.lei
 * @since 2018年06月21日 下午12:25
 */
@Component
@Slf4j
public class DmsMetadataServer implements InitializingBean {

    private final Environment environment;

    public DmsMetadataServer(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String zookeeperHost = SysEnvUtil.SOA_ZOOKEEPER_HOST;

        if (zookeeperHost != null) {
            log.info("zk host in the environment is already set...");
        } else {
            zookeeperHost = environment.getProperty(SysEnvUtil.KEY_SOA_ZOOKEEPER_HOST);
            if (zookeeperHost == null) {
                log.info("zk host in the environment is not found,setting it with spring boot application, host is {}");
                System.setProperty(SysEnvUtil.KEY_SOA_ZOOKEEPER_HOST, "127.0.0.1:2181");
            }
        }
        ZkBootstrap zkBootstrap = new ZkBootstrap();
        zkBootstrap.openApiInit();
    }
}
