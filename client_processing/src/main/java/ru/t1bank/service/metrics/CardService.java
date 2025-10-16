package ru.t1bank.service.metrics;

import ru.t1bank.dto.CardDto;

import java.util.Optional;

public interface CardService {

    void create(CardDto dto);

    Optional<CardDto> getCardById(Long id);
}
