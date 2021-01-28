package com.xuanhe.prize.api.config;

import com.alibaba.fastjson.JSON;
import com.xuanhe.prize.commons.config.RedisKeys;
import com.xuanhe.prize.commons.util.RedisUtils;
import com.xuanhe.prize.commons.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class LoginIntercetor implements HandlerInterceptor {
    @Autowired
    RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //无论访问的地址是不是正确的，都进行登录验证，登录成功后的访问再进行分发，404的访问自然会进入到错误控制器中
        HttpSession session = request.getSession();
        if (session.getAttribute("loginUserId") != null) {
            try {
                //验证当前请求的session是否是已登录的session
                String loginSessionId = (String) redisUtils.get("loginUser:" +  session.getAttribute("loginUserId"));
                if (loginSessionId != null && loginSessionId.equals(session.getId())) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response401(response);
        return false;
    }

    private void response401(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        try {
            response.getWriter().print(JSON.toJSONString(new Result(-1, "用户未登录！", null)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
