package com.micropay.payment.mapper;

import com.micropay.payment.dto.transaction.InitiateTransactionEvent;
import com.micropay.payment.dto.transaction.TransactionFailedEvent;
import com.micropay.payment.dto.transaction.TransactionSucceededEvent;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionEventMapper {

    @Mapping(expression = "java( UUID.randomUUID())", target = "eventId")
    @Mapping(source = "payment.id", target = "paymentId")
    InitiateTransactionEvent mapToInitiateTransactionEvent(Payment payment);

    @Mapping(expression = "java( UUID.randomUUID())", target = "eventId")
    TransactionSucceededEvent mapToTransactionSucceededEvent(PaymentModel payment);

    @Mapping(expression = "java( UUID.randomUUID())", target = "eventId")
    @Mapping(source = "payment.transactionId", target = "transactionId")
    @Mapping(source = "failureReason", target = "failureReason")
    TransactionFailedEvent mapToTransactionFailedEvent(PaymentModel payment, String failureReason);
}
