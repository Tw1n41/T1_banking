package ru.t1bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.t1bank.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("payment_date")
    private LocalDate paymentDate;

    @JsonProperty("is_credit")
    private Boolean isCredit;

    @JsonProperty("payed_at")
    private LocalDateTime payedAt;

    @JsonProperty("payment_status")
    private PaymentStatus paymentStatus;

    @JsonProperty("expired")
    private boolean expired;

    @JsonProperty("message_key")
    private String messageKey;

}
