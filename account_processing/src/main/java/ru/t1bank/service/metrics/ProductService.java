package ru.t1bank.service.metrics;

import ru.t1bank.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts();
}
