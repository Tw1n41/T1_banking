package ru.t1bank.service.impl;

import com.opencsv.bean.processor.PreAssignmentProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.t1bank.Product;
import ru.t1bank.aop.annotation.Cached;
import ru.t1bank.aop.annotation.Metric;
import ru.t1bank.repository.ProductRepository;
import ru.t1bank.service.metrics.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Metric
    @PreAuthorize("hasAuthority('MASTER')")
    public Product createProduct(Product product) {
        log.info("Сохранен продукт {}", product);
        return productRepository.save(product);
    }

    @Override
    @Cached
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт не найден, id=" + id));
    }

    @Override
    @Cached
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
