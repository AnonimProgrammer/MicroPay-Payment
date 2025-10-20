package com.micropay.payment.service.messaging.outbox;

import com.micropay.payment.model.event.entity.BaseEvent;

public interface OutboxService {

    <T> BaseEvent saveBaseEvent(String eventType, String exchangeName, String routingKey, T payload);

    void publish(BaseEvent event);

}
