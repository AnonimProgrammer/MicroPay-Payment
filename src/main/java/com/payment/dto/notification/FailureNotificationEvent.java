package com.payment.dto.notification;

public class FailureNotificationEvent {

    private Long walletId;
    private Long userId;
    private String title;
    private String content;

    public FailureNotificationEvent() {}
    public FailureNotificationEvent(Long walletId, Long userId, String title, String content) {
        this.walletId = walletId;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "FailureNotificationEvent{" +
                "walletId=" + walletId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
