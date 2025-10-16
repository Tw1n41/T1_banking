package ru.t1bank.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.t1bank.Product;
import ru.t1bank.aop.annotation.Cached;
import ru.t1bank.aop.annotation.Metric;
import ru.t1bank.dto.ProductDto;
import ru.t1bank.kafka.KafkaClientProducer;
import ru.t1bank.kafka.KafkaProductProducer;
import ru.t1bank.repository.ProductRepository;
import ru.t1bank.service.metrics.ProductService;
import ru.t1bank.util.ProductMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final KafkaProductProducer kafkaProductProducer;
    private final ProductMapper productMapper;

    @Value("${t1bank.kafka.topic.product}")
    private String productTopic;

    @Override
    @Metric
    @PreAuthorize("hasAuthority('MASTER')")
    public ProductDto createProduct(ProductDto productDto) {
//        Product saved = Product.builder()
//                .name(productDto.getName())
//                .productKey(productDto.getProductKey())
//                .createDate(LocalDateTime.now())
//                .build();

        Product product = productMapper.toEntity(productDto);

        if (product.getProductId() == null) {
            product.setProductId(product.getProductKey()
                    + "_" + UUID.randomUUID());
        }

        Product saved = productRepository.save(product);

        try {
            kafkaProductProducer.sendProductCreated(saved);
        }
        catch (Exception e) {
            log.error("Ошибка отправки продукта {} в Kafka: {}", saved.getId(), e.getMessage(), e);
        }

        return productMapper.toDto(saved);
    }

    @Override
    @Cached
    public Optional<ProductDto> getProdById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto);
    }

    @Override
    @Cached
    public Optional<ProductDto> getProducts(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto);
    }

    @Override
    @Metric
    @PreAuthorize("hasAuthority('CURRENT_CLIENT')")
    public ProductDto updProduct(Long id, ProductDto productDto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт с id = " + id + " не найден"));

        // применяем изменения через DTO
        product.setName(productDto.getName());
        product.setProductKey(productDto.getProductKey());
        product.setCreateDate(productDto.getCreateDate());

        Product updated = productRepository.save(product);
        kafkaProductProducer.sendProductUpdated(updated);
        return productMapper.toDto(updated);
    }

    @Override
    @Metric
    @PreAuthorize("hasAuthority('MASTER')")
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт с id=" + id + " не найден"));

        productRepository.delete(product);
        kafkaProductProducer.sendProductDeleted(product);
    }
}
