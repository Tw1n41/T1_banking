import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1bank.dto.CreditProductRequestDto;
import ru.t1bank.service.metrics.CreditProductService;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CreditProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CreditProductService creditProductService;

    @Test
    void createCreditProduct_shouldReturnOk() throws Exception {
        CreditProductRequestDto dto = new CreditProductRequestDto();
        dto.setClientId(1L);
        dto.setAmount(20000.0);
        dto.setInterestRate(BigDecimal.valueOf(12));
        dto.setMonthCount(6);

        mockMvc.perform(post("/credit-products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(creditProductService, times(1)).processCreditProduct(any());
    }
}
