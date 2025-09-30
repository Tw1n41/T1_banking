package ru.t1bank.util;

import org.springframework.stereotype.Component;
import ru.t1bank.Transaction;
import ru.t1bank.dto.TransactionDto;
import ru.t1bank.enums.TransactionType;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionDto dto) {
        if (dto == null) return null;

        return Transaction.builder()
                .id(dto.getTransactionId())
                .accountId(dto.getAccountId())
                .amount(dto.getAmount())
                .transactionDate(dto.getTransactionDate())
                .transactionType(dto.getType() != null ? TransactionType.valueOf(dto.getType()) : null)
                .build();
    }

    public TransactionDto toDto(Transaction entity) {
        if (entity == null) return null;

        return TransactionDto.builder()
                .transactionId(entity.getId())
                .accountId(entity.getAccountId())
                .amount(entity.getAmount())
                .transactionDate(entity.getTransactionDate())
                .type(entity.getTransactionType() != null ? entity.getTransactionType().name() : null)
                .build();
    }
}
