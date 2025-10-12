package ru.t1bank.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1bank.Account;
import ru.t1bank.Card;
import ru.t1bank.dto.CardDto;
import ru.t1bank.repository.AccountRepository;
import ru.t1bank.repository.CardRepository;
import ru.t1bank.util.CardMapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClientCardsConsumer {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final AccountRepository accountRepository;

    @KafkaListener(topics = "${ru.t1bank.kafka.topic.client_cards}", groupId = "account-processing")
    public void consume(CardDto dto) {
        log.info("Получено сообщение {}", dto);

        boolean accountBlocked = accountRepository.findById(dto.getAccountId())
                .map(Account::isBlocked)
                .orElse(true);

        if (accountBlocked) {
            log.warn("Счёт {} заблокирован. Карта не создаётся.", dto.getAccountId());
            return;
        }

        Card card = cardMapper.toEntity(dto);
        Card saved = cardRepository.save(card);
        log.info("Карта создана {}", saved);
    }
}
