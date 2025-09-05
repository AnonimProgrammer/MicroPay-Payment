package com.payment.dto.notification;

public class SuccessNotificationEvent {

    private Long walletId;
    private Long userId;
    private String title;
    private String content;

    public SuccessNotificationEvent() {}
    public SuccessNotificationEvent(Long walletId, Long userId, String title, String content) {
        this.walletId = walletId;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SuccessNotificationEvent{" +
                "walletId=" + walletId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
