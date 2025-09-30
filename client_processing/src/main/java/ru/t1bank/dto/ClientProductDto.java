package ru.t1bank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1bank.enums.ProductKey;
import ru.t1bank.enums.ProductStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class ClientProductDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("product_key")
    private ProductKey productKey;

    @JsonProperty("product_key")
    private ProductStatus productStatus;

    @JsonProperty("open_date")
    private LocalDateTime openDate;

    @JsonProperty("close_date")
    private LocalDateTime closeDate;

}
