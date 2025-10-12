package ru.t1bank.service.metrics;

import ru.t1bank.dto.CreditProductRequestDto;

public interface CreditProductService {

    void processCreditProduct(CreditProductRequestDto dto);
}
