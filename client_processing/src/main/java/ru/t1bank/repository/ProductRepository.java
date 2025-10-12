package ru.t1bank.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ru.t1bank.Client;
import ru.t1bank.Product;
import ru.t1bank.enums.ProductKey;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findByProductId(Long productId);

    List<Product> findByProductKey(ProductKey productKey);

    boolean existsByProductId(Long productId);

    List<Product> getAllProducts();
}
