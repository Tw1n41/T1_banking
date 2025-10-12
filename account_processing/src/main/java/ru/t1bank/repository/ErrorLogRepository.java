package ru.t1bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1bank.aop.ErrorLog;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {
}
