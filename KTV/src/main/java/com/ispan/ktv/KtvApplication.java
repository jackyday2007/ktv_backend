package com.ispan.ktv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

<<<<<<< HEAD
@SpringBootApplication
//(exclude = {SecurityAutoConfiguration.class})
=======
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
>>>>>>> 44d51a4b390661866ad32173a031a476383d1efa
public class KtvApplication {

	public static void main(String[] args) {
		SpringApplication.run(KtvApplication.class, args);
	}

}
