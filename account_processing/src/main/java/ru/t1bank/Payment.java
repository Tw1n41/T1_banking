package ru.t1bank;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import ru.t1bank.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "is_credit")
    private Boolean isCredit;

    @Column(name = "payed_at")
    private LocalDateTime payedAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "expired")
    private boolean expired;

    @Column(name = "message_key")
    private String messageKey;
}
