package com.ispan.ktv.util;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JsonWebTokenInterceptor implements HandlerInterceptor {
	
	@Autowired
	private JsonWebTokenUtility jsonWebTokenUtility;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		String method = request.getMethod();
//		if(!"OPTIONS".equalsIgnoreCase(method)) {
//			String auth = request.getHeader("Authorization");
//			JSONObject result = processAuthorizationHeader(auth);
//			if(result==null || result.length()==0) {
//				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//				response.setHeader("Access-Control-Allow-Credentials", "true");
//				response.setHeader("Access-Control-Allow-Origin", "*");
//				response.setHeader("Access-Control-Allow-Headers", "*");
//				return false;
//			}
//		}
		return true;
		
	}
	
	private JSONObject processAuthorizationHeader(String auth) {
		if(auth!=null && auth.length()!=0) {
			auth = auth.substring(7);  //'Bearer '
			String result = jsonWebTokenUtility.validateEncryptedToken(auth);
			if(result!=null) {
				return new JSONObject(result);
			}
		}
		return null;
	}
	

}
