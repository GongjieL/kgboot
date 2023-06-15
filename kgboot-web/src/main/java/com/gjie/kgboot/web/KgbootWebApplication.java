package com.gjie.kgboot.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = {"com.gjie.kgboot"})
@PropertySource({"classpath:application.properties",
        "classpath:application-common.properties",
        "classpath:application-util.properties",
        "classpath:application-api.properties",
        "classpath:application-mysql-data.properties",
        "classpath:application-dao.properties"})
@MapperScan({"com.gjie.kgboot.dao"})
@EnableKafka
public class KgbootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(KgbootWebApplication.class, args);
    }

}
