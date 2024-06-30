package com.lubase.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class }, scanBasePackages = "com.lubase.*")
public class LubaseSsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LubaseSsoApplication.class, args);
	}

}
