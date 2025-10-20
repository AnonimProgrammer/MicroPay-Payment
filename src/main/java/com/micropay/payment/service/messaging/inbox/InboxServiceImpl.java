package com.micropay.payment.service.messaging.inbox;

import com.micropay.payment.model.event.entity.InboxEntity;
import com.micropay.payment.repo.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxServiceImpl implements InboxService {

    private final InboxRepository inboxRepository;

    @Override
    @Transactional
    public void checkInbox(UUID eventId){
        inboxRepository.findById(eventId).ifPresent(inbox -> {
            throw new RuntimeException("Inbox already contains such event.");
        });
        InboxEntity inboxEntity = new InboxEntity();
        inboxEntity.setEventId(eventId);

        inboxRepository.save(inboxEntity);
    }
}
