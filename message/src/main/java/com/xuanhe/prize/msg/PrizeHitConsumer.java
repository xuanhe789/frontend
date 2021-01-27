package com.xuanhe.prize.msg;

import com.rabbitmq.client.Channel;
import com.xuanhe.prize.commons.config.RabbitKeys;
import com.xuanhe.prize.commons.db.entity.CardUserGame;
import com.xuanhe.prize.commons.db.entity.CardUserHit;
import com.xuanhe.prize.commons.db.mapper.CardUserGameMapper;
import com.xuanhe.prize.commons.db.mapper.CardUserHitMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class PrizeHitConsumer {
    @Autowired
    private CardUserHitMapper cardUserHitMapper;
    @RabbitListener(queues = RabbitKeys.QUEUE_HIT)
    public void process(CardUserHit cardUserHit, Channel channel, Message message) throws IOException {
        try {
            cardUserHitMapper.insert(cardUserHit);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            log.info("中奖记录入库成功,消息消费成功");
        } catch (Throwable e) {
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            log.error(message+"中奖记录入库失败，重新入列");
        }

    }

}
