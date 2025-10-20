package com.micropay.payment.repo;

import com.micropay.payment.model.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryExtension {
}
