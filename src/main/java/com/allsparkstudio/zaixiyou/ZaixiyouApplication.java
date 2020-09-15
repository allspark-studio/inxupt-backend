package com.allsparkstudio.zaixiyou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableConfigurationProperties
@EnableOpenApi
@MapperScan(basePackages = "com.allsparkstudio.zaixiyou.dao")
public class ZaixiyouApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZaixiyouApplication.class, args);
    }

}
