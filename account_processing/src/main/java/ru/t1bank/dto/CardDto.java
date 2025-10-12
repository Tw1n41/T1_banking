package ru.t1bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.t1bank.enums.CardStatus;
import ru.t1bank.enums.PaymentSystems;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("card_id")
    private String cardId;

    @JsonProperty("payment_system")
    private PaymentSystems paymentSystem;

    @JsonProperty("card_status")
    private CardStatus cardStatus;
}
