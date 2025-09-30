package ru.t1bank.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.t1bank.ProductRegistry;
import ru.t1bank.dto.ClientDto;
import ru.t1bank.dto.CreditProductRequestDto;
import ru.t1bank.repository.ProductRegistryRepository;
import ru.t1bank.service.metrics.CreditProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditProductServiceImpl implements CreditProductService {

    private final ProductRegistryRepository productRegistryRepository;
    private final RestTemplate restTemplate;

    @Value("${client.processing.url}")
    private String clientServiceUrl;

    @Value("${credit.limit}")
    private Double creditLimit;

    @Override
    public void processCreditProduct(CreditProductRequestDto dto) {
        log.info("Обработка кредитного продукта {}", dto);

        ClientDto client = restTemplate.getForObject(
                clientServiceUrl + "/client/" + dto.getClientId(),
                ClientDto.class
        );

        if (client == null) {
            log.warn("Клиент {} не найден", dto.getClientId());
            return;
        }

        List<ProductRegistry> existingProducts = productRegistryRepository.findByClientId(dto.getClientId());
        double totalDebt = existingProducts.stream()
                .mapToDouble(ProductRegistry::getAmount)
                .sum() + dto.getAmount();

        boolean hasOverdue = existingProducts.stream().anyMatch(ProductRegistry::isOverdue);

        if (totalDebt > creditLimit) {
            log.warn("Суммарная задолженность {} превышает лимит {}", totalDebt, creditLimit);
            return;
        }

        if (hasOverdue) {
            log.warn("У клиента есть просрочки, новый продукт не одобрен");
            return;
        }

        double monthlyRate = dto.getInterestRate().doubleValue() / 12 / 100;
        int n = dto.getMonthCount();
        double annuity = dto.getAmount() *
                (monthlyRate * Math.pow(1 + monthlyRate, n)) /
                (Math.pow(1 + monthlyRate, n) - 1);

        ProductRegistry registry = ProductRegistry.builder()
                .clientId(dto.getClientId())
                .amount(dto.getAmount())
                .monthCount(n)
                .monthlyPayment(BigDecimal.valueOf(annuity))
                .interestRate(dto.getInterestRate())
                .openDate(LocalDateTime.now())
                .build();

        productRegistryRepository.save(registry);
        log.info("Продукт создан для клиента {} с ежемесячным платежом {}", dto.getClientId(), annuity);
    }
}
