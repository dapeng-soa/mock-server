package com.github.dapeng.dms;

import com.github.dapeng.dms.sql.MockDataSource;
import com.github.dapeng.dms.sql.MockDataSource$;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@EnableSwagger2Doc
@SpringBootApplication
@PropertySource({"classpath:db.properties", "classpath:oss.properties"})
public class DapengMockServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DapengMockServerApplication.class, args);
    }

    @Bean
    public MockDataSource$ getMockDataSource() {
        return MockDataSource.getInstance();
    }

    /**
     * 针对前端ajax的消息转换器
     *
     * @return
     */
   /* @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> list = new ArrayList();
        list.add(MediaType.TEXT_HTML);
        list.add(MediaType.TEXT_PLAIN);
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingConverter.setSupportedMediaTypes(list);
        return mappingConverter;
    }*/
}
