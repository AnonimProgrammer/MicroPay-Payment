package com.micropay.payment.config;

public class RabbitConstants {

    // WALLET SERVICE CONSTANTS
    public static final String WALLET_DEBIT_EXCHANGE = "wallet.debit.event";
    public static final String WALLET_CREDIT_EXCHANGE = "wallet.credit.event";
    public static final String WALLET_REFUND_EXCHANGE = "wallet.refund.event";

    public static final String WALLET_DEBITED_QUEUE = "wallet.debited.queue";
    public static final String WALLET_DEBIT_FAILED_QUEUE = "wallet.debit.failed.queue";
    public static final String WALLET_CREDITED_QUEUE = "wallet.credited.queue";
    public static final String WALLET_CREDIT_FAILED_QUEUE = "wallet.credit.failed.queue";
    public static final String WALLET_REFUNDED_QUEUE = "wallet.refunded.queue";

    public static final String WALLET_DEBIT_ROUTING_KEY = "wallet.debit";
    public static final String WALLET_CREDIT_ROUTING_KEY = "wallet.credit";
    public static final String WALLET_REFUND_ROUTING_KEY = "wallet.refund";

    // TRANSACTION SERVICE CONSTANTS
    public static final String TRANSACTION_EXCHANGE = "transaction.event";

    public static final String TRANSACTION_CREATED_QUEUE = "transaction.created.queue";

    public static final String TRANSACTION_INITIATE_ROUTING_KEY = "transaction.initiate";
    public static final String TRANSACTION_FAILED_ROUTING_KEY = "transaction.failed";
    public static final String TRANSACTION_SUCCEEDED_ROUTING_KEY = "transaction.succeeded";

    // NOTIFICATION SERVICE CONSTANTS
    public static final String NOTIFICATION_EXCHANGE = "notification.event";

    public static final String NOTIFICATION_SUCCESS_ROUTING_KEY = "notification.success";
    public static final String NOTIFICATION_FAILURE_ROUTING_KEY = "notification.failure";

}
