package ru.t1bank.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ru.t1bank.ClientProduct;

import java.util.Optional;

@Repository
public interface ClientProductRepository extends JpaRepository<ClientProduct,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ClientProduct> findByStatus(String status);
}
