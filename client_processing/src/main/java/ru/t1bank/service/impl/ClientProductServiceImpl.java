package ru.t1bank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.t1bank.Client;
import ru.t1bank.ClientProduct;
import ru.t1bank.Product;
import ru.t1bank.aop.annotation.Cached;
import ru.t1bank.aop.annotation.Metric;
import ru.t1bank.dto.ClientProductDto;
import ru.t1bank.kafka.KafkaClientProductProducer;
import ru.t1bank.repository.ClientProductRepository;
import ru.t1bank.repository.ClientRepository;
import ru.t1bank.repository.ProductRepository;
import ru.t1bank.service.metrics.ClientProductService;
import ru.t1bank.util.ClientProductMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientProductServiceImpl implements ClientProductService {

    private final ClientProductRepository clientProductRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ClientProductMapper clientProductMapper;
    private final KafkaClientProductProducer kafkaTemplate;

    private final ClientHttpConnector clientHttpConnector;

    @Value("${ru.t1bank.kafka.topic.client_products}")
    private String clientProductsTopic;

    @Value("${ru.t1bank.kafka.topic.client_credit_products}")
    private String clientCreditProductsTopic;

    @Override
    @Metric
    @PreAuthorize("hasAuthority('MASTER')")
    public ClientProductDto create(ClientProductDto clientPrDto) {

        Client client = clientRepository.findById(clientPrDto.getClientId())
                .orElseThrow(() -> new RuntimeException("Клиент " + clientPrDto.getClientId() + " не найден"));

        Product prod = productRepository.findById(clientPrDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Продукт " + clientPrDto.getProductId() + " не найден"));

        ClientProduct clP = clientProductMapper.toEntity(clientPrDto);
        clP.setClient(client);
        clP.setProduct(prod);
        clP.setOpenDate(LocalDateTime.now());

        ClientProduct saved = clientProductRepository.save(clP);

        try {
            kafkaTemplate.send(clientProductMapper.toDto(saved));
        }
        catch (Exception e){
            throw new RuntimeException("Ошибка при создании " + saved);
        }

        return clientProductMapper.toDto(saved);
    }

    @Override
    @Cached
    @PreAuthorize("hasAuthority('MASTER')")
    public Optional<ClientProductDto> getById(Long id) {
        return clientProductRepository.findById(id)
                .map(clientProductMapper::toDto);
    }

    @Override
    @Cached
    @PreAuthorize("hasAuthority('MASTER')")
    public List<ClientProductDto> getAllServices() {
        return clientProductRepository.findAll().stream()
                .map(clientProductMapper::toDto)
                .toList();
    }

    @Override
    @Metric
    @PreAuthorize("hasAuthority('CURRENT_CLIENT')")
    public ClientProductDto updService(Long id, ClientProductDto clientPrDto) {

        ClientProduct clientProduct = clientProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Клиент " + id + " не найден"));

        if (clientPrDto.getCloseDate() != null) {
            clientProduct.setCloseDate(clientPrDto.getCloseDate());
        }

        if (clientPrDto.getProductStatus() != null) {
            clientProduct.setProductStatus(clientProduct.getProductStatus());
        }

        ClientProduct upd = clientProductRepository.save(clientProduct);

        try {
            kafkaTemplate.send(clientProductMapper.toDto(upd));
        }
        catch (Exception e){
            throw new RuntimeException("Ошибка при обновлении " + upd);
        }

        return clientProductMapper.toDto(upd);
    }

    @Override
    @Metric
    @PreAuthorize("hasAuthority('MASTER')")
    public void delService(Long id) {
        clientProductRepository.findById(id).ifPresentOrElse(clientProduct -> {
            clientProductRepository.delete(clientProduct);
            kafkaTemplate.send(clientProductMapper.toDto(clientProduct));
            log.info("Удалён ClientProduct {}", id);
        }, () -> {
            throw new RuntimeException("ClientProduct " + id + " не найден");
        });
    }
}
