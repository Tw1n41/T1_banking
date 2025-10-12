package ru.t1bank;

import jakarta.persistence.*;
import lombok.*;
import ru.t1bank.enums.ProductKey;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_key", nullable = false)
    private ProductKey productKey;

    @Column(name = "name")
    private String name;

    @Column(name = "create_date")
    private LocalDateTime createDate;
}
