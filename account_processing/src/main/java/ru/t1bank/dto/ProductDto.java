package ru.t1bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.t1bank.enums.ProductKey;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @JsonProperty("external_id")
    private Long externalId;

    @JsonProperty("product_key")
    private ProductKey productKey;

    @JsonProperty("name")
    private String name;

    @JsonProperty("create_date")
    private LocalDateTime createDate;
}
