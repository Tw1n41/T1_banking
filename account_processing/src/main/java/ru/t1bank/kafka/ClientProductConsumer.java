package ru.t1bank.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1bank.Product;
import ru.t1bank.dto.ProductDto;
import ru.t1bank.service.metrics.ProductService;
import ru.t1bank.util.ProductMapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClientProductConsumer {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @KafkaListener(topics = "${ru.t1bank.kafka.topic.client_products}", groupId = "account-processing")
    public void consume(ProductDto dto) {
        log.info("Получено сообщение из client_products: {}", dto);

        try {
            Product product = productMapper.toEntity(dto);
            productService.createProduct(product);
            log.info("Продукт создан {}", product.getId());
        } catch (Exception e) {
            log.error("Ошибка при создании продукта ", e);
        }
    }
}
