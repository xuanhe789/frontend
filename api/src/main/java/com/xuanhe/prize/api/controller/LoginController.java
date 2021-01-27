package com.xuanhe.prize.api.controller;

import com.xuanhe.prize.commons.config.RedisKeys;
import com.xuanhe.prize.commons.db.entity.CardUser;
import com.xuanhe.prize.commons.db.mapper.CardUserMapper;
import com.xuanhe.prize.commons.util.PasswordUtil;
import com.xuanhe.prize.commons.util.RedisUtils;
import com.xuanhe.prize.commons.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api")
@Api(tags = {"登录模块"})
public class LoginController {
    @Autowired
    private CardUserMapper cardUserMapper;
    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="account",value = "用户名",required = true),
            @ApiImplicitParam(name="password",value = "密码",required = true)
    })
    public Result login(HttpServletRequest request,String account,String password){
        Integer errortimes = (Integer) redisUtils.get(RedisKeys.USERLOGINTIMES+account);
        //判断用户登录是否密码错误5次
        if (errortimes != null && errortimes >= 5){
            return new Result(0, "密码错误5次，请5分钟后再登录",null);
        }
        CardUser user=cardUserMapper.selectByUsernameAndPassword(account, PasswordUtil.encodePassword(password));
        //去掉用户的敏感信息，存入redis
        if (user==null){
            //如果密码错误，错误次数加1，达到5次账号锁定5分钟
            redisUtils.incr(RedisKeys.USERLOGINTIMES+account,1);
            //设置锁定时间
            redisUtils.expire(RedisKeys.USERLOGINTIMES+account,300);
        }
        user.setIdcard(null);
        user.setPasswd(null);
        HttpSession session = request.getSession();
        session.setAttribute("loginUserId",user.getId());
        redisUtils.set("loginUser:" + user.getId(), session.getId());
        redisUtils.set(RedisKeys.SESSIONID+session.getId(),user);
        return new Result(1, "登录成功",user);
    }

    @GetMapping("/logout")
    @ApiOperation(value = "退出")
    public Result logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session!=null){
            redisUtils.del("loginUser:"+session.getAttribute("loginUserId"));
            session.invalidate();
        }
        return new Result(1, "退出成功",null);
    }
}
