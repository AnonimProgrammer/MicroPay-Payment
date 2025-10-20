package com.micropay.payment.repo;

import com.micropay.payment.model.event.entity.InboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InboxRepository extends JpaRepository<InboxEntity, UUID> {

}
