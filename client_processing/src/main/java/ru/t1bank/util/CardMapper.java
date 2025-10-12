package ru.t1bank.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.t1bank.Card;
import ru.t1bank.dto.CardDto;

@Component
@Slf4j
public class CardMapper {

    public CardDto toDto(Card entity) {
        if (entity == null) return null;
        return CardDto.builder()
                .id(entity.getId())
                .accountId(entity.getAccountId())
                .cardType(entity.getCardType())
                .issueDate(entity.getIssueDate())
                .expirationDate(entity.getExpirationDate())
                .build();
    }

    public Card toEntity(CardDto dto) {
        if (dto == null) return null;
        return Card.builder()
                .id(dto.getId())
                .accountId(dto.getAccountId())
                .cardType(dto.getCardType())
                .issueDate(dto.getIssueDate())
                .expirationDate(dto.getExpirationDate())
                .build();
    }
}
