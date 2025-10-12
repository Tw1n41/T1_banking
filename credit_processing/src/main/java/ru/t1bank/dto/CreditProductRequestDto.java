package ru.t1bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditProductRequestDto {

    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("month_count")
    private Integer monthCount;

    @JsonProperty("interest_rate")
    private BigDecimal interestRate;

    @JsonProperty("overdue")
    private Boolean overdue;
}
