package ru.t1bank;

import jakarta.persistence.*;
import lombok.*;
import ru.t1bank.enums.AccountStatus;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "is_recalc")
    private Boolean isRecalc;

    @Column(name = "card_exist")
    private Boolean cardExist;

    @Column(name = "blocked")
    private boolean blocked;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
}
