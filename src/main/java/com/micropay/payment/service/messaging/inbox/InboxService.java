package com.micropay.payment.service.messaging.inbox;

import java.util.UUID;

public interface InboxService {

    void checkInbox(UUID eventId);
}
