package com.micropay.payment.service.messaging.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micropay.payment.model.event.EventStatus;
import com.micropay.payment.model.event.entity.BaseEvent;
import com.micropay.payment.repo.BaseEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final BaseEventRepository baseEventRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    private static final int MAX_RETRIES = 5;

    @Override
    @Transactional
    public <T> BaseEvent saveBaseEvent(String eventType, String exchangeName, String routingKey, T payload) {
        BaseEvent event = new BaseEvent();
        event.setEventType(eventType);
        event.setExchangeName(exchangeName);
        event.setRoutingKey(routingKey);
        event.setPayload(payload);

        return baseEventRepository.save(event);
    }

    @Override
    @Transactional
    public void publish(BaseEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    event.getExchangeName(), event.getRoutingKey(),
                    mapper.readValue(event.getPayload(), Object.class)
            );
            event.setStatus(EventStatus.SENT);
            log.info("Event published successfully. Event type: {}, ID: {}",
                    event.getEventType(), event.getId());
        } catch (Exception exception) {
            event.setRetryCount(event.getRetryCount() + 1);

            if (event.getRetryCount() > MAX_RETRIES) {
                event.setStatus(EventStatus.FAILED);
                log.error("Event failed after max retries. Event type: {}, ID: {}",
                        event.getEventType(), event.getId());
            } else {
                event.setStatus(EventStatus.RETRYING);
                log.warn("Event failed to publish. Will retry. ID: {}, Attempt {}",
                        event.getId(), event.getRetryCount());
            }
        }
        baseEventRepository.save(event);
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void retryPendingEvents() {
        List<BaseEvent> pendingEvents = baseEventRepository.findPendingEvents();
        if (pendingEvents.isEmpty()) return;

        log.info("Retrying {} pending events", pendingEvents.size());
        for (BaseEvent event : pendingEvents) {
            publish(event);
        }
    }
}
