package com.ispan.ktv;
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
		registry.addInterceptor(jsonWebTokenInterceptor)
		.addPathPatterns("/ktvbackend/**");
	}
	
	

}
