package com.ispan.ktv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
//(exclude = {SecurityAutoConfiguration.class})
public class KtvApplication {

	public static void main(String[] args) {
		SpringApplication.run(KtvApplication.class, args);
	}

}
