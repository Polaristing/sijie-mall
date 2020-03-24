package com.sijie.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Kim
 * @联系QQ 1172895463
 * @email gting0518@163.com
 * @link https://www.xiayuan52.cn
 */
@MapperScan("com.sijie.mall.dao")
@SpringBootApplication
public class SiJieMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(SiJieMallApplication.class, args);
    }
}
