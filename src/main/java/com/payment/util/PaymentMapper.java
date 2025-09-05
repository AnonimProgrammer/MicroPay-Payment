package com.payment.util;

import com.payment.dto.payment.internal.response.PaymentResponse;
import com.payment.model.payment.PaymentModel;
import com.payment.model.payment.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentModel toModel(Payment entity);

    Payment toEntity(PaymentModel model);

    PaymentResponse toResponse(Payment model);
}
