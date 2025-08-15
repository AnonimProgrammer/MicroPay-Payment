package com.docker.payment.util;

import com.docker.payment.model.payment.PaymentModel;
import com.docker.payment.model.payment.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentModel toModel(Payment entity);

    Payment toEntity(PaymentModel model);
}
