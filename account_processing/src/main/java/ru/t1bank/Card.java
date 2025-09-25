package ru.t1bank;

import jakarta.persistence.*;
import lombok.*;
import ru.t1bank.enums.CardStatus;
import ru.t1bank.enums.PaymentSystems;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "card_id")
    private String cardId;

    @Enumerated(EnumType.STRING)
    private PaymentSystems paymentSystem;

    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;
}
