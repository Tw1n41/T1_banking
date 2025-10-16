import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.t1bank.ClientProduct;
import ru.t1bank.dto.ClientProductDto;
import ru.t1bank.repository.ClientProductRepository;
import ru.t1bank.service.impl.ClientProductServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class ClientProductServiceImplTest {
    @Mock
    private ClientProductRepository repository;

    @InjectMocks
    private ClientProductServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByClientId() {
        when(repository.findByClientId(1L)).thenReturn(List.of(new ClientProduct()));
        List<ClientProductDto> result = service.findByClientId(1L);
        assertEquals(1, result.size());
    }
}
