package com.allsparkstudio.zaixiyou.consumer;

import com.allsparkstudio.zaixiyou.dao.EventRemindMapper;
import com.allsparkstudio.zaixiyou.pojo.po.EventRemind;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queuesToDeclare = @Queue(value = "eventRemind"))
public class EventRemindConsumer {

    @Autowired
    EventRemindMapper eventRemindMapper;

    @RabbitHandler
    public void addEvent(EventRemind remind) {
        eventRemindMapper.insertSelective(remind);
    }
}
