package com.micropay.payment.mapper;

import com.micropay.payment.dto.wallet.credit.CreditWalletEvent;
import com.micropay.payment.dto.wallet.debit.DebitWalletEvent;
import com.micropay.payment.dto.wallet.refund.RefundWalletEvent;
import com.micropay.payment.model.payment.PaymentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface WalletEventMapper {

    @Mapping(expression = "java( UUID.randomUUID())", target = "eventId")
    @Mapping(source = "payment.id", target = "paymentId")
    @Mapping(source = "payment.destination", target = "walletId", qualifiedByName = "stringToLong")
    CreditWalletEvent mapToCreditWalletEvent(PaymentModel payment);

    @Mapping(expression = "java( UUID.randomUUID())", target = "eventId")
    @Mapping(source = "payment.id", target = "paymentId")
    @Mapping(source = "payment.source", target = "walletId", qualifiedByName = "stringToLong")
    DebitWalletEvent mapToDebitWalletEvent(PaymentModel payment);

    @Mapping(expression = "java( UUID.randomUUID())", target = "eventId")
    @Mapping(source = "payment.id", target = "paymentId")
    @Mapping(source = "payment.source", target = "walletId", qualifiedByName = "stringToLong")
    RefundWalletEvent mapToRefundWalletEvent(PaymentModel payment);

    @Named("stringToLong")
    default Long stringToLong(String value) {
        return value != null ? Long.valueOf(value) : null;
    }

}
