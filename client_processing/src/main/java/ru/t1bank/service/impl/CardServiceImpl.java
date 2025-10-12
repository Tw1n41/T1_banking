package ru.t1bank.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1bank.Card;
import ru.t1bank.aop.annotation.Metric;
import ru.t1bank.dto.CardDto;
import ru.t1bank.kafka.KafkaCardProducer;
import ru.t1bank.repository.CardRepository;
import ru.t1bank.service.metrics.CardService;
import ru.t1bank.util.CardMapper;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final KafkaCardProducer kafkaCardProducer;

    @Override
    @Metric
    public void create(CardDto cardDto) {

        if (cardDto.getClientId() == null) {
            throw new IllegalArgumentException("clientId обязателен");
        }
        if (cardDto.getAccountId() == null) {
            throw new IllegalArgumentException("accountId обязателен");
        }

        try {
            kafkaCardProducer.send(cardDto);
        }
        catch (Exception e){
            log.warn("Не удалось сохранить карту {}", cardDto);
            throw new RuntimeException();
        }

    }

}
