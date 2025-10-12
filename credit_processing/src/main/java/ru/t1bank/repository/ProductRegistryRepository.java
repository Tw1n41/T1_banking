package ru.t1bank.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ru.t1bank.ProductRegistry;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRegistryRepository extends JpaRepository<ProductRegistry, Long> {

    List<ProductRegistry> findByClientId(Long clientId);

    List<ProductRegistry> findByClientIdAndOverdueTrue(Long clientId);
}
