package com.micropay.payment.mapper;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentMapperTest {

    private PaymentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PaymentMapperImpl();
    }

    @Test
    void toModel_ShouldMapAllFields() {
        Payment entity = new Payment();
        entity.setId(1L);
        entity.setTransactionId(UUID.randomUUID());
        entity.setAmount(BigDecimal.valueOf(100));
        entity.setStatus(PaymentStatus.COMPLETED);
        entity.setSource("source");
        entity.setSourceType(EndpointType.CARD);
        entity.setDestination("destination");
        entity.setDestinationType(EndpointType.WALLET);
        entity.setType(TransactionType.TOP_UP);
        entity.setFailureReason(null);
        entity.setDebitCompleted(true);
        entity.setCreditCompleted(true);
        entity.setRefundCompleted(false);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        PaymentModel model = mapper.toModel(entity);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo(entity.getId());
        assertThat(model.getTransactionId()).isEqualTo(entity.getTransactionId());
        assertThat(model.getAmount()).isEqualTo(entity.getAmount());
        assertThat(model.getStatus()).isEqualTo(entity.getStatus());
        assertThat(model.getSource()).isEqualTo(entity.getSource());
        assertThat(model.getSourceType()).isEqualTo(entity.getSourceType());
        assertThat(model.getDestination()).isEqualTo(entity.getDestination());
        assertThat(model.getDestinationType()).isEqualTo(entity.getDestinationType());
        assertThat(model.getType()).isEqualTo(entity.getType());
        assertThat(model.getFailureReason()).isEqualTo(entity.getFailureReason());
        assertThat(model.isDebitCompleted()).isEqualTo(entity.isDebitCompleted());
        assertThat(model.isCreditCompleted()).isEqualTo(entity.isCreditCompleted());
        assertThat(model.isRefundCompleted()).isEqualTo(entity.isRefundCompleted());
        assertThat(model.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(model.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    void toEntity_ShouldMapAllFieldsFromRequest() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(BigDecimal.valueOf(100));
        request.setSource("source");
        request.setSourceType(EndpointType.CARD);
        request.setDestination("destination");
        request.setDestinationType(EndpointType.WALLET);
        request.setType(TransactionType.TOP_UP);

        Payment entity = mapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getAmount()).isEqualTo(request.getAmount());
        assertThat(entity.getSource()).isEqualTo(request.getSource());
        assertThat(entity.getSourceType()).isEqualTo(request.getSourceType());
        assertThat(entity.getDestination()).isEqualTo(request.getDestination());
        assertThat(entity.getDestinationType()).isEqualTo(request.getDestinationType());
        assertThat(entity.getType()).isEqualTo(request.getType());
    }

    @Test
    void toResponse_ShouldMapAllFields() {
        Payment entity = new Payment();
        entity.setId(10L);
        entity.setAmount(BigDecimal.valueOf(100));
        entity.setStatus(PaymentStatus.COMPLETED);
        entity.setSource("source");
        entity.setSourceType(EndpointType.CARD);
        entity.setDestination("destination");
        entity.setDestinationType(EndpointType.WALLET);
        entity.setType(TransactionType.TOP_UP);
        entity.setCreatedAt(LocalDateTime.now());

        PaymentResponse response = mapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(entity.getId());
        assertThat(response.getAmount()).isEqualTo(entity.getAmount());
        assertThat(response.getStatus()).isEqualTo(entity.getStatus());
        assertThat(response.getSource()).isEqualTo(entity.getSource());
        assertThat(response.getSourceType()).isEqualTo(entity.getSourceType());
        assertThat(response.getDestination()).isEqualTo(entity.getDestination());
        assertThat(response.getDestinationType()).isEqualTo(entity.getDestinationType());
        assertThat(response.getType()).isEqualTo(entity.getType());
        assertThat(response.getCreatedAt()).isEqualTo(entity.getCreatedAt());
    }
}
