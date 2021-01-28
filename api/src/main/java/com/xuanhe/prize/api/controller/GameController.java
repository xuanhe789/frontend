package com.xuanhe.prize.api.controller;

import com.github.pagehelper.PageHelper;
import com.xuanhe.prize.commons.db.entity.CardGame;
import com.xuanhe.prize.commons.db.entity.CardGameExample;
import com.xuanhe.prize.commons.db.entity.CardProductDto;
import com.xuanhe.prize.commons.db.entity.ViewCardUserHit;
import com.xuanhe.prize.commons.db.mapper.CardGameMapper;
import com.xuanhe.prize.commons.db.mapper.ViewCardUserHitMapper;
import com.xuanhe.prize.commons.util.PageBean;
import com.xuanhe.prize.commons.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/game")
@Api(tags = {"活动模块"})
public class GameController {
    @Autowired
    private CardGameMapper cardGameMapper;
    @Autowired
    private CardGameMapper gameMapper;
    @Autowired
    private ViewCardUserHitMapper viewCardUserHitMapper;

    @GetMapping("/list/{status}/{curpage}/{limit}")
    @ApiOperation(value = "活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="status",value = "活动状态（-1=全部，0=未开始，1=进行中，2=已结束）",example = "-1",required = true),
            @ApiImplicitParam(name = "curpage",value = "第几页",defaultValue = "1",dataType = "int", example = "1",required = true),
            @ApiImplicitParam(name = "limit",value = "每页条数",defaultValue = "10",dataType = "int",example = "3",required = true)
    })
    public Result list(@PathVariable int status, @PathVariable int curpage, @PathVariable int limit) {
        Date now = new Date();
        CardGameExample example = new CardGameExample();
        CardGameExample.Criteria c = example.createCriteria();
        switch (status) {
            case -1:
                //查全部
                break;
            case 0:
                //未开始
                c.andStarttimeGreaterThan(now);break;
            case 1:
                //进行中
                c.andStarttimeLessThanOrEqualTo(now).andEndtimeGreaterThan(now);break;
            case 2:
                //已结束
                c.andEndtimeLessThanOrEqualTo(now);break;
        }
        long total = gameMapper.countByExample(example);
        example.setOrderByClause("starttime desc");
        PageHelper.startPage(curpage, limit);
        return new Result(1,"成功",new PageBean<CardGame>(curpage,limit,total,gameMapper.selectByExample(example)));
    }

    @GetMapping("/hit/{gameid}/{curpage}/{limit}")
    @ApiOperation(value = "中奖列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",dataType = "int",example = "1",required = true),
            @ApiImplicitParam(name = "curpage",value = "第几页",defaultValue = "1",dataType = "int", example = "1",required = true),
            @ApiImplicitParam(name = "limit",value = "每页条数",defaultValue = "10",dataType = "int",example = "3",required = true)
    })
    public Result<PageBean<ViewCardUserHit>> hit(@PathVariable int gameid, @PathVariable int curpage, @PathVariable int limit) {
        long total=viewCardUserHitMapper.getTotalByGameid(gameid);
        PageHelper.startPage(curpage,limit);
        List<ViewCardUserHit> viewCardUserHits= viewCardUserHitMapper.selectByGameId(gameid);
        PageBean<ViewCardUserHit> pageBean = new PageBean<>(curpage, limit, total, viewCardUserHits);
        return new Result(1,"成功",pageBean);
    }

    @GetMapping("/info/{gameid}")
    @ApiOperation(value = "活动信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public Result<CardGame> info(@PathVariable int gameid) {
        return new Result(1,"成功",gameMapper.selectByPrimaryKey(gameid));
    }

    @GetMapping("/products/{gameid}")
    @ApiOperation(value = "奖品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameid",value = "活动id",example = "1",required = true)
    })
    public Result<List<CardProductDto>> products(@PathVariable int gameid) {
        return new Result(1,"成功",cardGameMapper.getCardProductDtoByGameId(gameid));
    }

}
