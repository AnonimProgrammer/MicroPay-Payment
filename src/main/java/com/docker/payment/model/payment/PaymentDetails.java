package com.docker.payment.model.payment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "detailsType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CardDetails.class, name = "CARD"),
        @JsonSubTypes.Type(value = WalletDetails.class, name = "WALLET")
})
public interface PaymentDetails {
}
