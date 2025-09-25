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
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "key")
    private ProductKey productKey;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "product_id")
    private String productId;

    @PrePersist
    public void generatePID() {
        if (productId == null && id != null) {
            this.productId = productKey + id.toString();
        }
    }

}
