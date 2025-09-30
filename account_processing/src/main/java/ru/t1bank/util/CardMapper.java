package ru.t1bank.util;

import org.springframework.stereotype.Component;
import ru.t1bank.Account;
import ru.t1bank.Card;
import ru.t1bank.dto.CardDto;

@Component
public class CardMapper {

    public CardDto toDto(Card entity) {
        if (entity == null) return null;

        return CardDto.builder()
                .id(entity.getId())
                .accountId(entity.getAccountId())
                .cardId(entity.getCardId())
                .paymentSystem(entity.getPaymentSystem())
                .cardStatus(entity.getCardStatus())
                .build();
    }

    public Card toEntity(CardDto dto) {
        if (dto == null) return null;

        return Card.builder()
                .id(dto.getId())
                .accountId(dto.getAccountId())
                .cardId(dto.getCardId())
                .paymentSystem(dto.getPaymentSystem())
                .cardStatus(dto.getCardStatus())
                .build();
    }
}
