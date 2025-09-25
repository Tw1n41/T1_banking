package ru.t1bank;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_registry")
public class PaymentRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_registry_id")
    private Long productRegistryId;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "interest_rate_amount")
    private BigDecimal interestRateAmount;

    @Column(name = "debt_amount")
    private BigDecimal debtAmount;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "payment_expiration_date")
    private LocalDateTime paymentExpirationDate;
}
