package com.fkgou.ShoppingPayment.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 支付启动类
 * 
 * @author Poi
 *
 */
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@MapperScan("com.fkgou.ShoppingPayment.web.dao")
public class ShoppingPaymentApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ShoppingPaymentApplication.class, args);
	}

}
