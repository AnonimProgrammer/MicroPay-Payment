package com.micropay.payment.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessNotificationEvent {

    private Long walletId;
    private String title;
    private String content;

    @Override
    public String toString() {
        return "SuccessNotificationEvent{" +
                "walletId=" + walletId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
