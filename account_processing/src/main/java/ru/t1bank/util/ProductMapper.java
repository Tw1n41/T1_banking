package ru.t1bank.util;

import org.springframework.stereotype.Component;
import ru.t1bank.Product;
import ru.t1bank.dto.ProductDto;

import java.time.LocalDateTime;

@Component
public class ProductMapper {

    public Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        return Product.builder()
                .externalId(dto.getExternalId())
                .productKey(dto.getProductKey())
                .name(dto.getName())
                .createDate(dto.getCreateDate() != null ? dto.getCreateDate() : LocalDateTime.now())
                .build();
    }

    public ProductDto toDto(Product entity) {
        if (entity == null) {
            return null;
        }
        return ProductDto.builder()
                .externalId(entity.getExternalId())
                .productKey(entity.getProductKey())
                .name(entity.getName())
                .createDate(entity.getCreateDate())
                .build();
    }
}
