package com.ispan.ktv;
<<<<<<< HEAD
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import com.ispan.ktv.util.JsonWebTokenFilter;
//
//@Configuration
//public class SpringBootJavaConfig implements WebMvcConfigurer {
//	
//	@Autowired
//	private JsonWebTokenFilter jsonWebTokenInterceptor;
//	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		System.out.println("registry = " + registry);
//		registry.addInterceptor(jsonWebTokenInterceptor)
//		.addPathPatterns("/ktvbackend/**");
//	}
//	
//	
//
//}
=======

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ispan.ktv.util.JsonWebTokenInterceptor;

@Configuration
public class SpringBootJavaConfig implements WebMvcConfigurer {

	@Autowired
	private JsonWebTokenInterceptor jsonWebTokenInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println("registry = " + registry);
		registry.addInterceptor(jsonWebTokenInterceptor)
				.addPathPatterns("/ktvbackend/**");
	}

}
>>>>>>> 44d51a4b390661866ad32173a031a476383d1efa
