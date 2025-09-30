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
public class ProductRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "profuct_id")
    private Long productId;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "open_date")
    private LocalDateTime openDate;

    @Column(name = "month_count")
    private Integer monthCount;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "overdue")
    private Boolean overdue;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    public Boolean isOverdue() { return this.overdue; }
}
