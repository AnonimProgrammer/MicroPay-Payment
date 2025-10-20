package com.micropay.payment.repo.impl;

import com.micropay.payment.model.payment.PaymentStatus;
import com.micropay.payment.model.payment.entity.Payment;
import com.micropay.payment.model.transaction.TransactionType;
import com.micropay.payment.repo.PaymentRepositoryExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryExtensionImpl implements PaymentRepositoryExtension {

    private final EntityManager entityManager;

    @Override
    public List<Payment> findPaymentsByWalletIdAndCursor(String walletId, LocalDateTime cursor, int limit) {
        StringBuilder jpql = new StringBuilder("""
            SELECT p
            FROM Payment p
            WHERE (
                (p.sourceType = 'WALLET' AND p.source = :walletId)
                OR (p.destinationType = 'WALLET' AND p.destination = :walletId)
            )
        """);
        cursorDateIsNotNull(cursor, jpql);
        jpql.append(" ORDER BY p.createdAt DESC");

        TypedQuery<Payment> query = entityManager.createQuery(jpql.toString(), Payment.class)
                .setParameter("walletId", walletId)
                .setMaxResults(limit + 1);

        if (cursor != null) {
            query.setParameter("cursorDate", cursor);
        }
        return query.getResultList();
    }

    @Override
    public List<Payment> findPayments(
            Long walletId, TransactionType transactionType,
            PaymentStatus paymentStatus,
            LocalDateTime cursorDate, int limit
    ) {
        StringBuilder jpql = new StringBuilder("SELECT p FROM Payment p WHERE 1=1");

        walletIdIsNotNull(walletId, jpql);
        transactionTypeIsNotNull(transactionType, jpql);
        paymentStatusIsNotNull(paymentStatus, jpql);
        cursorDateIsNotNull(cursorDate, jpql);
        jpql.append(" ORDER BY p.createdAt DESC");

        TypedQuery<Payment> query = entityManager.createQuery(jpql.toString(), Payment.class)
                .setMaxResults(limit + 1);

        if (walletId != null) {
            query.setParameter("walletId", walletId.toString());
        }
        if (transactionType != null) {
            query.setParameter("transactionType", transactionType);
        }
        if (paymentStatus != null) {
            query.setParameter("paymentStatus", paymentStatus);
        }
        if (cursorDate != null) {
            query.setParameter("cursorDate", cursorDate);
        }
        return query.getResultList();
    }

    private void walletIdIsNotNull(Long walletId, StringBuilder jpql) {
        if (walletId != null) {
            jpql.append(" AND ((p.sourceType = 'WALLET' AND p.source = :walletId) ")
                    .append(" OR (p.destinationType = 'WALLET' AND p.destination = :walletId))");
        }
    }

    private void transactionTypeIsNotNull(TransactionType type, StringBuilder jpql) {
        if (type != null) {
            jpql.append(" AND p.type = :transactionType");
        }
    }

    private void paymentStatusIsNotNull(PaymentStatus status, StringBuilder jpql) {
        if (status != null) {
            jpql.append(" AND p.status = :paymentStatus");
        }
    }

    private void cursorDateIsNotNull(LocalDateTime cursorDate, StringBuilder jpql) {
        if (cursorDate != null) {
            jpql.append(" AND p.createdAt < :cursorDate");
        }
    }

}


