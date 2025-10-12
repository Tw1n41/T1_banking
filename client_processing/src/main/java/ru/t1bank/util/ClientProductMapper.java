package ru.t1bank.util;

import org.springframework.stereotype.Component;
import ru.t1bank.Client;
import ru.t1bank.ClientProduct;
import ru.t1bank.Product;
import ru.t1bank.dto.ClientProductDto;

@Component
public class ClientProductMapper {

    public ClientProductDto toDto(ClientProduct entity) {
        if (entity == null) {
            return null;
        }

        return ClientProductDto.builder()
                .id(entity.getId())
                .clientId(entity.getClient() != null ? entity.getClient().getId() : null)
                .productId(entity.getProduct() != null ? entity.getProduct().getId() : null)
                .openDate(entity.getOpenDate())
                .closeDate(entity.getCloseDate())
                .productStatus(entity.getProductStatus())
                .build();
    }

    public ClientProduct toEntity(ClientProductDto dto) {
        if (dto == null) {
            return null;
        }

        return ClientProduct.builder()
                .id(dto.getId())
                .openDate(dto.getOpenDate())
                .closeDate(dto.getCloseDate())
                .productStatus(dto.getProductStatus())
                .build();
    }
}
