import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import ru.t1bank.ProductRegistry;
import ru.t1bank.dto.ClientDto;
import ru.t1bank.dto.CreditProductRequestDto;
import ru.t1bank.repository.ProductRegistryRepository;
import ru.t1bank.service.impl.CreditProductServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreditProductServiceImplTest {

    @Mock
    private ProductRegistryRepository productRegistryRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CreditProductServiceImpl creditProductService;

    @Value("${client.processing.url}")
    private String clientServiceUrl = "http://localhost:8081";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        creditProductService = new CreditProductServiceImpl(productRegistryRepository, restTemplate);
    }

    @Test
    void processCreditProduct_shouldSaveProduct_whenClientIsValid() {
        // given
        CreditProductRequestDto request = new CreditProductRequestDto();
        request.setClientId(1L);
        request.setAmount(50000.0);
        request.setMonthCount(12);
        request.setInterestRate(BigDecimal.valueOf(12.0));

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setFirstName("John");

        when(restTemplate.getForObject(
                eq(clientServiceUrl + "/client/" + request.getClientId()), eq(ClientDto.class)))
                .thenReturn(clientDto);

        when(productRegistryRepository.findByClientId(request.getClientId()))
                .thenReturn(List.of());

        // when
        creditProductService.processCreditProduct(request);

        // then
        verify(productRegistryRepository, times(1)).save(any(ProductRegistry.class));
    }

    @Test
    void processCreditProduct_shouldNotSave_whenClientNotFound() {
        CreditProductRequestDto request = new CreditProductRequestDto();
        request.setClientId(99L);
        request.setAmount(20000.0);
        request.setInterestRate(BigDecimal.valueOf(10.0));
        request.setMonthCount(10);

        when(restTemplate.getForObject(anyString(), eq(ClientDto.class)))
                .thenReturn(null);

        creditProductService.processCreditProduct(request);

        verify(productRegistryRepository, never()).save(any());
    }

    @Test
    void processCreditProduct_shouldNotSave_whenLimitExceeded() {
        CreditProductRequestDto request = new CreditProductRequestDto();
        request.setClientId(1L);
        request.setAmount(90000.0);
        request.setInterestRate(BigDecimal.valueOf(10.0));
        request.setMonthCount(12);

        ClientDto client = new ClientDto();
        client.setId(1L);

        ProductRegistry existing = ProductRegistry.builder()
                .clientId(1L)
                .amount(30000.0)
                .openDate(LocalDateTime.now())
                .build();

        when(restTemplate.getForObject(anyString(), eq(ClientDto.class)))
                .thenReturn(client);
        when(productRegistryRepository.findByClientId(1L))
                .thenReturn(List.of(existing));

        creditProductService.processCreditProduct(request);

        verify(productRegistryRepository, never()).save(any());
    }

    @Test
    void processCreditProduct_shouldNotSave_whenOverdueExists() {
        CreditProductRequestDto request = new CreditProductRequestDto();
        request.setClientId(1L);
        request.setAmount(10000.0);
        request.setInterestRate(BigDecimal.valueOf(10.0));
        request.setMonthCount(6);

        ClientDto client = new ClientDto();
        client.setId(1L);

        ProductRegistry overdue = ProductRegistry.builder()
                .clientId(1L)
                .amount(5000.0)
                .overdue(true)
                .openDate(LocalDateTime.now())
                .build();

        when(restTemplate.getForObject(anyString(), eq(ClientDto.class)))
                .thenReturn(client);
        when(productRegistryRepository.findByClientId(1L))
                .thenReturn(List.of(overdue));

        creditProductService.processCreditProduct(request);

        verify(productRegistryRepository, never()).save(any());
    }
}
