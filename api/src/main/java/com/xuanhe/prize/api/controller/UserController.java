package com.xuanhe.prize.api.controller;

import com.github.pagehelper.PageHelper;
import com.xuanhe.prize.commons.config.RedisKeys;
import com.xuanhe.prize.commons.db.entity.CardUser;
import com.xuanhe.prize.commons.db.entity.CardUserDto;
import com.xuanhe.prize.commons.db.entity.ViewCardUserHit;
import com.xuanhe.prize.commons.db.mapper.CardUserGameMapper;
import com.xuanhe.prize.commons.db.mapper.ViewCardUserHitMapper;
import com.xuanhe.prize.commons.util.PageBean;
import com.xuanhe.prize.commons.util.RedisUtils;
import com.xuanhe.prize.commons.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
@Api(tags = {"用户模块"})
public class UserController {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ViewCardUserHitMapper hitMapper;
    @Autowired
    private CardUserGameMapper cardUserGameMapper;

    @GetMapping("/info")
    @ApiOperation(value = "用户信息")
    public Result info(HttpServletRequest request) {
        HttpSession session = request.getSession();
        CardUser user = (CardUser) redisUtils.get(RedisKeys.SESSIONID + session.getId());
        if (user==null){
            return new Result(0,"登录信息过时，请重新登录",null);
        }
        CardUserDto cardUserDto = new CardUserDto(user);
        //设置用户参与的活动次数
        cardUserDto.setGames(cardUserGameMapper.getGamesNumByUserId(cardUserDto.getId()));
        //设置用户获奖的次数
        cardUserDto.setProducts(hitMapper.getProductsNumByUserId(cardUserDto.getId()));
        return new Result(1, "成功",cardUserDto);
    }

    @GetMapping("/hit/{gameid}/{curpage}/{limit}")
    @ApiOperation(value = "我的奖品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id（-1=全部）",dataType = "int",example = "1",required = true),
            @ApiImplicitParam(name = "curpage",value = "第几页",defaultValue = "1",dataType = "int", example = "1"),
            @ApiImplicitParam(name = "limit",value = "每页条数",defaultValue = "10",dataType = "int",example = "3")
    })
    public Result hit(@PathVariable int gameid, @PathVariable int curpage, @PathVariable int limit, HttpServletRequest request) {
        HttpSession session = request.getSession();
        CardUser user = (CardUser) redisUtils.get(RedisKeys.SESSIONID + session.getId());
        if (user==null){
            return new Result(0,"登录信息过时，请重新登录",null);
        }
        long total=hitMapper.getTotal(user.getId(),gameid);
        PageHelper.startPage(curpage, limit);
        List<ViewCardUserHit> cardUserHits= hitMapper.selectByUserIdAndGameId(user.getId(),gameid);
        return new Result(1,"成功",new PageBean<ViewCardUserHit>(curpage,limit,total,cardUserHits));
    }
}
