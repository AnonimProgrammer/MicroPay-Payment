package com.micropay.payment.mapper;

import com.micropay.payment.dto.payment.internal.request.PaymentRequest;
import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentModel toModel(Payment entity);

    Payment toEntity(PaymentRequest request);

    PaymentResponse toResponse(Payment entity);

}
