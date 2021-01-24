package com.xuanhe.prize.job.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.xuanhe.prize.commons.db.entity.*;
import com.xuanhe.prize.commons.db.mapper.CardGameMapper;
import com.xuanhe.prize.commons.db.mapper.CardGameProductMapper;
import com.xuanhe.prize.commons.db.mapper.CardGameRulesMapper;
import com.xuanhe.prize.commons.util.RedisUtils;
import com.xuanhe.prize.job.annotation.ElasticSimpleJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 活动信息预热，每隔1分钟执行一次
 * 查找未来1分钟内（含），要开始的活动
 */
@ElasticSimpleJob(cron = "0 * * * * ?")
@Component
@Slf4j
public class GameTask implements SimpleJob {
    @Autowired
    private CardGameMapper cardGameMapper;
    @Autowired
    private CardGameProductMapper cardGameProductMapper;
    @Autowired
    private CardGameRulesMapper cardGameRulesMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void execute(ShardingContext shardingContext) {
        //当前时间
        Date now = new Date();
        //一分钟时间内
        Date minuteLater = new Date(now.getTime() + 60000);
        List<CardGame> cardGames = cardGameMapper.selectByNowTime(now, minuteLater);
        if (cardGames.size()==0){
            log.info("game list scan : size = 0");
            return;
        }
        log.info("game list scan : size = {}",cardGames.size());
        //有相关活动数据，则将活动数据预热进redis
        cardGames.forEach(cardGame -> {
            //活动开始时间
            long start = cardGame.getStarttime().getTime();
            //活动结束时间
            long end = cardGame.getEndtime().getTime();
            //计算活动结束时间到现在还有多少秒，作为redis key过期时间
            long expire=(end-start)/1000;
            //活动持续时间（ms）
            long duration = end - start;
            //设置活动的状态为1，活动无法再编辑
            cardGame.setStatus(1);
            //将活动信息存入redis
            redisUtils.set(RedisUtils.INFO+cardGame.getId(),cardGame,-1);
            log.info("load game info:{},{},{},{}", cardGame.getId(),cardGame.getTitle(),cardGame.getStarttime(),cardGame.getEndtime());

            //活动奖品信息
            List<CardProductDto> cardProductDtos = cardGameMapper.getCardProductDtoByGameId(cardGame.getId());
            Map<Integer, CardProduct> productMap = cardProductDtos.stream().collect(Collectors.toMap(cardProductDto -> cardProductDto.getId(), cardProductDto -> cardProductDto));
            log.info("load product type:{}",productMap.size());
            //活动配置信息
            List<CardGameProduct> cardGameProducts = cardGameProductMapper.selectByGameId(cardGame.getId());
            log.info("load bind product:{}",cardGameProducts.size());

            //令牌桶
            List<Long> tokenList = new ArrayList();
            cardGameProducts.forEach(cardGameProduct -> {
                //生成amount个start到end之间的随机时间戳做令牌
                for (int i=0;i<cardGameProduct.getAmount();i++){
                    //真实的时间戳,奖品一多可能就会重复
                    long rnd=start+new Random().nextInt((int)duration);
                    //生成二次时间戳
                    //为什么乘1000，再额外加一个随机数呢？ - 防止时间段奖品多时重复
                    //记得取令牌判断时间时，除以1000，还原真正的时间戳
                    long token = rnd * 1000 + new Random().nextInt(999);
                    //将令牌放进令牌桶
                    tokenList.add(token);
                    //以令牌做key，对应的商品为value，创建redis缓存
                    log.info("token -> game : {} -> {}",token/1000 ,productMap.get(cardGameProduct.getProductid()).getName());
                    redisUtils.set(RedisUtils.TOKEN+cardGameProduct.getGameid()+"_"+token,productMap.get(cardGameProduct.getProductid()),expire);
                }
            });
            //排序后放入队列
            Collections.sort(tokenList);
            log.info("load tokens:{}",tokenList);

            //从右侧压入队列，从左到右，时间戳逐个增大
            redisUtils.rightPushAll(RedisUtils.TOKENS+cardGame.getId(),tokenList);
            redisUtils.expire(RedisUtils.TOKENS+cardGame.getId(),expire);

            //奖品策略配置信息
            List<CardGameRules> cardGameRules = cardGameRulesMapper.getRulesByGameId(cardGame.getId());
            //遍历策略，存入redis hash
            cardGameRules.forEach(cardGameRules1 -> {
                redisUtils.hset(RedisUtils.MAXGOAL +cardGame.getId(),cardGameRules1.getUserlevel()+"",cardGameRules1.getGoalTimes());
                redisUtils.hset(RedisUtils.MAXENTER +cardGame.getId(),cardGameRules1.getUserlevel()+"",cardGameRules1.getEnterTimes());
                log.info("load rules:level={},enter={},goal={}",cardGameRules1.getUserlevel(),cardGameRules1.getEnterTimes(),cardGameRules1.getGoalTimes());
            });
            redisUtils.expire(RedisUtils.MAXGOAL +cardGame.getId(),expire);
            redisUtils.expire(RedisUtils.MAXENTER +cardGame.getId(),expire);

             //活动状态变更为已预热，禁止管理后台再随便变动
            cardGame.setStatus(1);
            cardGameMapper.updateByPrimaryKey(cardGame);
                }
                //活动状态变更为已预热，禁止管理后台再随便变动

        );

    }
}
