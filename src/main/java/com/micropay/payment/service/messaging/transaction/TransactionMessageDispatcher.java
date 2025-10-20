package com.micropay.payment.service.messaging.transaction;

import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.dto.transaction.TransactionFailedEvent;
import com.micropay.payment.dto.transaction.TransactionSucceededEvent;

public interface TransactionMessageDispatcher {

    void publishInitiateTransactionEvent(InitiateTransactionEvent event);

    void publishTransactionSucceededEvent(TransactionSucceededEvent event);

    void publishTransactionFailedEvent(TransactionFailedEvent event);

}
