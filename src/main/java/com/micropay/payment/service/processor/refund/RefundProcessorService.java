package com.micropay.payment.service.processor.refund;

import com.micropay.payment.model.payment.PaymentModel;
import com.micropay.payment.service.processor.BaseProcessorService;

public interface RefundProcessorService extends BaseProcessorService {

    void processRefund(PaymentModel payment);
}
