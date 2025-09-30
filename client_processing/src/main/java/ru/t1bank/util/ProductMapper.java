package ru.t1bank.util;

import org.springframework.stereotype.Component;
import ru.t1bank.Product;
import ru.t1bank.dto.ProductDto;

@Component
public class ProductMapper {

    public Product toEntity(ProductDto dto) {
        return Product.builder()
                .name(dto.getName())
                .productKey(dto.getProductKey())
                .createDate(dto.getCreateDate())
                .build();
    }

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .productKey(product.getProductKey())
                .createDate(product.getCreateDate())
                .build();
    }
}
