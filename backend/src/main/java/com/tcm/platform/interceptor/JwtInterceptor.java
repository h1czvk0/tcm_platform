package com.tcm.platform.interceptor;

import com.alibaba.fastjson2.JSON;
import com.tcm.platform.common.Result;
import com.tcm.platform.common.ResultCode;
import com.tcm.platform.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        // 跨域OPTIONS请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        
        if (token == null || token.isEmpty()) {
            writeErrorResponse(response, ResultCode.TOKEN_MISSING);
            return false;
        }

        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            if (!jwtUtil.validateToken(token)) {
                writeErrorResponse(response, ResultCode.TOKEN_INVALID);
                return false;
            }
            
            // 将用户信息放入request
            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);
            
            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            request.setAttribute("userType", userType);
            
            return true;
        } catch (Exception e) {
            writeErrorResponse(response, ResultCode.TOKEN_INVALID);
            return false;
        }
    }

    private void writeErrorResponse(HttpServletResponse response, ResultCode resultCode) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(JSON.toJSONString(Result.error(resultCode)));
    }
}
