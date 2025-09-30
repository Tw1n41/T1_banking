package ru.t1bank.util;

import org.springframework.stereotype.Component;
import ru.t1bank.ProductRegistry;
import ru.t1bank.dto.CreditProductRequestDto;

@Component
public class CreditProductMapper {

    public ProductRegistry toEntity(CreditProductRequestDto dto, Client client) {
        if (dto == null) return null;

        return ProductRegistry.builder()
                .client(client)
                .productId(dto.getProductId())
                .amount(dto.getAmount())
                .monthCount(dto.getMonthCount())
                .interestRate(dto.getInterestRate())
                .build();
    }

    public CreditProductRequestDto toDto(ProductRegistry entity) {
        if (entity == null) return null;

        return CreditProductRequestDto.builder()
                .clientId(entity.getClient().getId())
                .productId(entity.getProductId())
                .amount(entity.getAmount())
                .monthCount(entity.getMonthCount())
                .interestRate(entity.getInterestRate())
                .build();
    }
}
