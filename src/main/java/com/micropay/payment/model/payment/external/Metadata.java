package com.micropay.payment.model.payment.external;

import com.micropay.payment.model.transaction.EndpointType;
import lombok.Data;

import java.util.UUID;

@Data
public class Metadata {
    private EndpointType source;
    private UUID operationId;
    private String initiatedBy;

    public Metadata(Builder builder) {
        this.source = builder.source;
        this.operationId = builder.operationId;
        this.initiatedBy = builder.initiatedBy;
    }

    public static class Builder {
        private EndpointType source;
        private UUID operationId;
        private String initiatedBy;

        public Builder source(EndpointType source) {
            this.source = source;
            return this;
        }
        public Builder operationId(UUID operationId) {
            this.operationId = operationId;
            return this;
        }
        public Builder initiatedBy(String initiatedBy) {
            this.initiatedBy = initiatedBy;
            return this;
        }

        public Metadata build() {return new Metadata(this);}
    }
}
