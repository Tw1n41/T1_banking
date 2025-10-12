package ru.t1bank.repository;

import jakarta.persistence.Id;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event;
import ru.t1bank.BlacklistRegistry;

import java.util.Optional;

@Repository
public interface BlacklistRegistryRepository extends JpaRepository<BlacklistRegistry, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    Optional<BlacklistRegistry> findById(Long Id);

    boolean existsByDocumentId(String documentId);
}
