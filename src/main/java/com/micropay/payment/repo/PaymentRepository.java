package com.micropay.payment.repo;

import com.micropay.payment.model.payment.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("""
        SELECT p
        FROM Payment p
        WHERE (p.sourceType = 'WALLET' AND p.source = :walletId)
        OR (p.destinationType = 'WALLET' AND p.destination = :walletId)
        ORDER BY p.createdAt DESC
    """)
    List<Payment> findPaymentsByWalletId(
            @Param("walletId") String walletId,
            Pageable pageable
    );

    @Query("""
        SELECT p
        FROM Payment p
        WHERE (
            (p.sourceType = 'WALLET' AND p.source = :walletId)
        OR (p.destinationType = 'WALLET' AND p.destination = :walletId)
        )
        AND p.createdAt < :cursorDate
        ORDER BY p.createdAt DESC
    """)
    List<Payment> findPaymentsByWalletIdAndCursor(
            @Param("walletId") String walletId,
            @Param("cursorDate") LocalDateTime cursorDate,
            Pageable pageable
    );



}
