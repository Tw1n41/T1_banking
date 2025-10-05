package ru.t1bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    @JsonProperty("transaction_id")
    private Long transactionId;

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("transaction_date")
    private LocalDateTime transactionDate;

    @JsonProperty("type")
    private String type;

    @JsonProperty("card_id")
    private Long cardId;

    @JsonProperty("month_count")
    private Integer monthCount;

    @JsonProperty("interest_rate")
    private BigDecimal interestRate;
}
