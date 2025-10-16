import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.t1bank.Client;
import ru.t1bank.dto.CheckResponce;
import ru.t1bank.dto.RegistryResponce;
import ru.t1bank.exception.ClientException;
import ru.t1bank.kafka.KafkaClientProducer;
import ru.t1bank.repository.BlacklistRegistryRepository;
import ru.t1bank.repository.ClientRepository;
import ru.t1bank.service.impl.ClientServiceImpl;
import ru.t1bank.util.JwtTokenUtil;
import ru.t1bank.web.CheckWebClient;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private KafkaClientProducer kafkaClientProducer;
    @Mock
    private CheckWebClient checkWebClient;
    @Mock
    private BlacklistRegistryRepository blacklistRegistry;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerClient_ShouldSaveClientAndSendKafka_WhenNotBlocked() {
        Client client = new Client();
        client.setClientId("123");

        when(blacklistRegistry.existsByDocumentId(any())).thenReturn(false);
        when(checkWebClient.check((int) anyLong())).thenReturn(Optional.of(new CheckResponce(false)));
        when(clientRepository.save(any())).thenReturn(client);
        when(jwtTokenUtil.generateToken(any(), any())).thenReturn("jwt-token");

        RegistryResponce response = clientService.registerClient(client);

        assertNotNull(response);
        verify(kafkaClientProducer, times(1)).send(any());
        verify(jwtTokenUtil, times(1)).generateToken(any(), eq("CURRENT_CLIENT"));
    }

    @Test
    void registerClient_ShouldThrowException_WhenClientBlacklisted() {
        Client client = new Client();
        when(blacklistRegistry.existsByDocumentId(any())).thenReturn(true);
        assertThrows(ClientException.class, () -> clientService.registerClient(client));
    }

    @Test
    void registerClient_ShouldThrowException_WhenClientBlocked() {
        Client client = new Client();
        when(checkWebClient.check((int) anyLong())).thenReturn(Optional.of(new CheckResponce(true)));
        assertThrows(ClientException.class, () -> clientService.registerClient(client));
    }
}
