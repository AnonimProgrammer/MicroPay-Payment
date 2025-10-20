package com.micropay.payment.dto.payment.internal.response;

import java.time.LocalDateTime;
import java.util.List;

public record CursorPage<T>(
        List<T> content,
        LocalDateTime nextCursor,
        boolean hasNext
) {}
