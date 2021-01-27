package com.xuanhe.prize.api.controller;

import com.xuanhe.prize.commons.config.RabbitKeys;
import com.xuanhe.prize.commons.config.RedisKeys;
import com.xuanhe.prize.commons.db.entity.*;
import com.xuanhe.prize.commons.db.mapper.CardGameMapper;
import com.xuanhe.prize.commons.db.mapper.CardGameRulesMapper;
import com.xuanhe.prize.commons.util.RedisUtils;
import com.xuanhe.prize.commons.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/act")
@Api(tags = {"抽奖模块"})
public class LotteryController {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    CardGameMapper cardGameMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/go/{gameid}")
    @ApiOperation(value = "抽奖")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public Result act(@PathVariable("gameid") int gameid, HttpServletRequest request){
        Date now = new Date();
        //获取活动信息
        CardGame cardGame = cardGameMapper.selectByPrimaryKey(gameid);
        //判断活动是否存在或者是否开始
        if (cardGame==null||cardGame.getStarttime().after(now)){
            return new Result(-1,"活动未开始",null);
        }
        //判断活动是否结束
        if (cardGame.getEndtime().before(now)){
            return new Result(-1,"活动已结束",null);
        }
        //获取当前用户
        HttpSession session = request.getSession();
        Integer userId= (Integer) session.getAttribute("loginUserId");
        CardUser user= (CardUser) redisUtils.get(RedisKeys.SESSIONID+userId);
        if (user==null){
            return new Result(-1,"尚未登陆，请先登录",null);
        }
        //判断该用户是否参与过此次活动，如果第一次参加，写进数据库，方便统计多少人参加了活动
        if (!redisUtils.hasKey(RedisKeys.USERGAME+userId+"_"+cardGame.getId())){
            redisUtils.set(RedisKeys.USERGAME+userId+"_"+cardGame.getId(),1,(cardGame.getEndtime().getTime()-now.getTime())/1000);
            //设置信息，存入数据库
            CardUserGame cardUserGame= new CardUserGame();
            cardUserGame.setCreatetime(new Date());
            cardUserGame.setUserid(userId);
            cardUserGame.setGameid(cardGame.getId());
            rabbitTemplate.convertAndSend(RabbitKeys.EXCHANGE_DIRECT,RabbitKeys.QUEUE_PLAY,cardUserGame);
        }
        //接下来判断用户的已中奖次数
        Integer count= (Integer) redisUtils.get(RedisKeys.USERHIT+gameid+"_"+userId);
        if (count == null){
            count = 0;
            redisUtils.set(RedisKeys.USERHIT+gameid+"_"+user.getId(),count,(cardGame.getEndtime().getTime() - now.getTime())/1000);
        }
        //获取当前用户会员等级所能中奖的最大次数
        Integer maxCount= (Integer) redisUtils.hget(RedisKeys.MAXGOAL+gameid,user.getLevel()+"");
        if (maxCount>0&&count>=maxCount){
            return new Result(-1,"你已达最大中奖次数",null);
        }
        //以上校验均通过,可以进入抽奖逻辑
        Long token= (Long) redisUtils.leftPop(RedisKeys.TOKENS+gameid);
        //如果token==null，说明奖品被抽光
        if (token==null){
            return new Result(-1,"奖品已被抽光",null);
        }
        //如果令牌时间戳大于当前时间，则未中奖，将令牌还回去
        if (now.getTime()<token/1000){
            redisUtils.leftPush(RedisKeys.TOKENS+gameid,token);
            return new Result(-1,"未中奖",null);
        }
        //走到这里，说明已中奖
        CardProduct cardProduct= (CardProduct) redisUtils.hget(RedisKeys.TOKEN+gameid,token+"");
        //该用户中奖次数+1
        redisUtils.incr(RedisKeys.USERHIT+gameid+"_"+userId,1);
        //将中奖信息投放到消息队列中，让消息队列来处理耗时的业务逻辑，如更新数据库
        CardUserHit cardUserHit = new CardUserHit();
        cardUserHit.setGameid(gameid);
        cardUserHit.setHittime(new Date());
        cardUserHit.setProductid(cardProduct.getId());
        cardUserHit.setUserid(userId);
        rabbitTemplate.convertAndSend(RabbitKeys.EXCHANGE_DIRECT,RabbitKeys.QUEUE_HIT,cardUserHit);
        //返回信息给前台
        return new Result(1,"恭喜中奖",null);
    }

    /**
     * 缓存信息监控
     * @param gameid
     * @return
     */
    @GetMapping("/info/{gameid}")
    @ApiOperation(value = "缓存信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public Result info(@PathVariable int gameid){
        Map map = new HashMap<>();
    }
}
