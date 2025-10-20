package com.micropay.payment.mapper;

import com.micropay.payment.dto.wallet.credit.CreditWalletEvent;
import com.micropay.payment.dto.wallet.debit.DebitWalletEvent;
import com.micropay.payment.dto.wallet.refund.RefundWalletEvent;
import com.micropay.payment.model.payment.PaymentModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class WalletEventMapperTest {

    private WalletEventMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new WalletEventMapperImpl();
    }

    @Test
    void mapToCreditWalletEvent_ShouldMapCorrectly() {
        PaymentModel payment = new PaymentModel();
        payment.setId(1L);
        payment.setDestination("12");
        payment.setAmount(new BigDecimal("150"));

        CreditWalletEvent event = mapper.mapToCreditWalletEvent(payment);

        assertThat(event).isNotNull();
        assertThat(event.eventId()).isNotNull();
        assertThat(event.paymentId()).isEqualTo(payment.getId());
        assertThat(event.walletId()).isEqualTo(12L);
        assertThat(event.amount()).isEqualTo(payment.getAmount());
    }

    @Test
    void mapToDebitWalletEvent_ShouldMapCorrectly() {
        PaymentModel payment = new PaymentModel();
        payment.setId(2L);
        payment.setSource("12");
        payment.setAmount(new BigDecimal("250"));

        DebitWalletEvent event = mapper.mapToDebitWalletEvent(payment);

        assertThat(event).isNotNull();
        assertThat(event.eventId()).isNotNull();
        assertThat(event.paymentId()).isEqualTo(payment.getId());
        assertThat(event.walletId()).isEqualTo(12L);
        assertThat(event.amount()).isEqualTo(payment.getAmount());
    }

    @Test
    void mapToRefundWalletEvent_ShouldMapCorrectly() {
        PaymentModel payment = new PaymentModel();
        payment.setId(3L);
        payment.setSource("12");
        payment.setAmount(new BigDecimal("350"));

        RefundWalletEvent event = mapper.mapToRefundWalletEvent(payment);

        assertThat(event).isNotNull();
        assertThat(event.eventId()).isNotNull();
        assertThat(event.paymentId()).isEqualTo(payment.getId());
        assertThat(event.walletId()).isEqualTo(12L);
        assertThat(event.amount()).isEqualTo(payment.getAmount());
    }

    @Test
    void mapMethods_ShouldReturnNullForNullPayment() {
        assertThat(mapper.mapToCreditWalletEvent(null)).isNull();
        assertThat(mapper.mapToDebitWalletEvent(null)).isNull();
        assertThat(mapper.mapToRefundWalletEvent(null)).isNull();
    }
}
