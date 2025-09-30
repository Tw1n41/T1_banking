package ru.t1bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.t1bank.enums.CardType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("card_type")
    private CardType cardType;

    @JsonProperty("issue_date")
    private LocalDateTime issueDate;

    @JsonProperty("expiration_date")
    private LocalDateTime expirationDate;
}
