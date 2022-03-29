package com.study.interceptor;

import com.study.limit.Idempotency;
import com.study.service.RedisService;
import com.study.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.*;

public class IdempotencyInterceptor implements HandlerInterceptor {

    @Autowired
    RedisService redisService;

    // 在业务处理器处理请求之前被调用 ， 被拦截的URL对应的方法执行前的自定义处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        try {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Annotation methodAnnotation = handlerMethod.getMethodAnnotation(Idempotency.class);
            if (Objects.nonNull(methodAnnotation)) {
                String token = getInterfaceInfo(request);
                Boolean flag = redisService.setNx(token, token);
                if (!flag) {
                    throw new Exception("接口重复提交");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return true;
    }

    // 在业务处理器处理请求完成之后，生成视图之前执行 ， 被拦截的URL对应的方法执行后的自定义处理
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        if ((handler instanceof HandlerMethod)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Annotation methodAnnotation = handlerMethod.getMethodAnnotation(Idempotency.class);
            if (Objects.nonNull(methodAnnotation)) {
                String token = getInterfaceInfo(request);
                redisService.removeKey(token);
            }

        }

    }

    // 在DispatcherServlet完全处理完请求之后被调用，可用于清理资源  表示此时modelAndView已被渲染，执行拦截器的自定义处理。
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    private String getInterfaceInfo(HttpServletRequest request) {
        String userId = "1001"; //可以理解为用户的表示，开发当中可以是获取用户的Token
        request.getRequestURI();
        Map ParameterMap = request.getParameterMap();
        Map reqMap = new HashMap();
        Set<Map.Entry<String, String[]>> entry = ParameterMap.entrySet();
        Iterator<Map.Entry<String, String[]>> it = entry.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> me = it.next();
            String key = me.getKey();
            String value = me.getValue()[0];
            reqMap.put(key, value);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(userId).append(request.getRequestURL()).append(reqMap.toString());
        return MD5Util.MD5Encode(sb.toString(), null);
    }
}
