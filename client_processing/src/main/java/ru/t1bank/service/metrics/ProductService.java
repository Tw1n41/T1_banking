package ru.t1bank.service.metrics;

import ru.t1bank.Product;
import ru.t1bank.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    Optional<ProductDto> getProdById(Long id);

    Optional<ProductDto> getProducts(Long id);

    ProductDto updProduct(Long id, ProductDto productDto);

    void deleteProduct(Long id);
}
