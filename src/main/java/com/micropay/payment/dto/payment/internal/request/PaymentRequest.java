package com.micropay.payment.dto.payment.internal.request;

import com.micropay.payment.model.payment.PaymentDetails;
import com.micropay.payment.model.transaction.EndpointType;
import com.micropay.payment.model.transaction.TransactionType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Amount must not be null")
    @Digits(integer = 5, fraction = 2,
            message = "Amount must be a valid monetary value with up to 5 digits before the decimal point and up to 2 decimal places")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private BigDecimal amount;

    @NotBlank(message = "Source must not be blank")
    private String source;

    @NotNull(message = "Source type must not be null")
    private EndpointType sourceType;

    @NotBlank(message = "Destination must not be blank")
    private String destination;

    @NotNull(message = "Destination type must not be null")
    private EndpointType destinationType;

    @NotNull(message = "Transaction type must not be null")
    private TransactionType type;

    @NotNull(message = "Payment details must not be null")
    private PaymentDetails paymentDetails;

}

