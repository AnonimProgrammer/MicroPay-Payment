package com.micropay.payment.repo;

import com.micropay.payment.model.event.entity.BaseEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BaseEventRepository extends JpaRepository<BaseEvent, Long> {

    @Modifying
    @Transactional
    @Query(value = """
    WITH locked_rows AS (
        SELECT id\s
        FROM base_events\s
        WHERE status IN ('NEW', 'RETRYING')
        ORDER BY created_at ASC
        LIMIT 50
        FOR UPDATE SKIP LOCKED
    )
    UPDATE base_events
    SET status = 'PROCESSING'
    WHERE id IN (SELECT id FROM locked_rows)
    RETURNING *
   \s""", nativeQuery = true)
    List<BaseEvent> findPendingEvents();

}
