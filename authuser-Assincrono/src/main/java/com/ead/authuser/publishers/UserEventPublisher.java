package com.ead.authuser.publishers;

import com.ead.authuser.dtos.UserEventDto;
import com.ead.authuser.enums.ActionType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value(value = "{ead.broker.exchange.userEvent}")
    private String exachangeUserEvent;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publisherUserEvent(UserEventDto userEventDto, ActionType actionType) {
        userEventDto.setActionType(actionType.toString());
        rabbitTemplate.convertAndSend(exachangeUserEvent, "", userEventDto);
    }
}
