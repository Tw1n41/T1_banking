package ru.t1bank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1bank.aop.annotation.HttpIncomeRequestLog;
import ru.t1bank.dto.CreditProductRequestDto;
import ru.t1bank.service.metrics.CreditProductService;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class CreditProductController {

    private final CreditProductService creditProductService;

    @PostMapping
    @HttpIncomeRequestLog
    public ResponseEntity<Void> createCreditProduct(@RequestBody CreditProductRequestDto dto) {
        creditProductService.processCreditProduct(dto);
        return ResponseEntity.ok().build();
    }
}
