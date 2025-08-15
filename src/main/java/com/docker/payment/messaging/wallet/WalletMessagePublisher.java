//package com.docker.payment.messaging.wallet;
//
//import com.docker.payment.dto.wallet.credit.CreditWalletEvent;
//import com.docker.payment.dto.wallet.debit.DebitWalletEvent;
//import com.docker.payment.dto.wallet.refund.RefundWalletEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class WalletMessagePublisher {
//
//    @Value("${rabbitmq.exchange.debit}")
//    private String debitExchange;
//    @Value("${rabbitmq.exchange.credit}")
//    private String creditExchange;
//    @Value("${rabbitmq.exchange.refund}")
//    private String refundExchange;
//    @Value("${rabbitmq.routing-key.wallet.debit}")
//    private String routingKeyDebit;
//    @Value("${rabbitmq.routing-key.wallet.credit}")
//    private String routingKeyCredit;
//    @Value("${rabbitmq.routing-key.wallet.refund}")
//    private String routingKeyRefund;
//
//    private final RabbitTemplate rabbitTemplate;
//    private final static Logger LOGGER = LoggerFactory.getLogger(WalletMessagePublisher.class);
//
//    public WalletMessagePublisher(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void publishDebitWalletEvent(DebitWalletEvent debitWalletEvent) {
//        LOGGER.info("[WalletMessagePublisher] - Publishing DebitWalletEvent: {}", debitWalletEvent);
//        rabbitTemplate.convertAndSend(debitExchange, routingKeyDebit, debitWalletEvent);
//    }
//
//    public void publishCreditWalletEvent(CreditWalletEvent creditWalletEvent) {
//        LOGGER.info("[WalletMessagePublisher] - Publishing CreditWalletEvent: {}", creditWalletEvent);
//        rabbitTemplate.convertAndSend(creditExchange, routingKeyCredit, creditWalletEvent);
//    }
//
//    public void publishRefundWalletEvent(RefundWalletEvent refundWalletEvent) {
//        LOGGER.info("[WalletMessagePublisher] - Publishing RefundWalletEvent: {}", refundWalletEvent);
//        rabbitTemplate.convertAndSend(refundExchange, routingKeyRefund, refundWalletEvent);
//    }
//
//}
