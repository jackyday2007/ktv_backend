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
<<<<<<< HEAD
=======
		System.out.println("registry = " + registry);
>>>>>>> 54243b4dc8bfaa393c1f0828a1b0e33a84e9be75
		registry.addInterceptor(jsonWebTokenInterceptor)
		.addPathPatterns("/ktvbackend/**");
	}
	
	

}
