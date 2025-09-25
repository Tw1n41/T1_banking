package ru.t1bank.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ru.t1bank.Account;
import ru.t1bank.Payment;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
