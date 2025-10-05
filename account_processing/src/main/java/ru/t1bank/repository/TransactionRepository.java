package ru.t1bank.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.t1bank.Payment;
import ru.t1bank.Transaction;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select count(t) from TransactionEntity t where t.cardId = :cardId and t.occurredAt >= :from and t.occurredAt <= :to")
    long countByCardIdBetween(@Param("cardId") String cardId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
