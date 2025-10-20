package com.micropay.payment.dto.notification;

import java.util.UUID;

public record SuccessNotificationEvent(
        UUID eventId,
        Long walletId,
        String title,
        String content
) {}
