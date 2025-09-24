package com.micropay.payment.util;

import com.micropay.payment.dto.payment.internal.response.PaymentResponse;
import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.model.payment.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentModel toModel(Payment entity);

    Payment toEntity(PaymentModel model);

    PaymentResponse toResponse(Payment model);
}
