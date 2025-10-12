package ru.t1bank.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.t1bank.Client;
import ru.t1bank.aop.annotation.HttpIncomeRequestLog;
import ru.t1bank.aop.annotation.HttpOutcomeRequestLog;
import ru.t1bank.dto.CheckResponce;
import ru.t1bank.dto.ClientDto;
import ru.t1bank.dto.RegistryResponce;
import ru.t1bank.exception.ClientException;
import ru.t1bank.kafka.KafkaClientProducer;
import ru.t1bank.repository.BlacklistRegistryRepository;
import ru.t1bank.repository.ClientRepository;
import ru.t1bank.service.metrics.ClientService;
import ru.t1bank.web.CheckWebClient;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final KafkaClientProducer kafkaClientProducer;
    private final CheckWebClient checkWebClient;
    private final BlacklistRegistryRepository blacklistRegistry;
    private final RestTemplate restTemplate;

    @HttpIncomeRequestLog
    @Transactional
    @Override
    public List<Client> registerClients(List<Client> clients) {
        List<Client> savedClients = new ArrayList<>();
        for (Client client : clients) {
            Optional<CheckResponce> check = check(Long.valueOf(client.getClientId()));
//            Optional<CheckResponce> check = checkWebClient.check(Integer.valueOf(client.getClientId()));
            check.ifPresent(checkResponse -> {
                if (!checkResponse.getBlocked()) {
                    Client saved = repository.save(client);
                    kafkaClientProducer.send(client.getId());
                    savedClients.add(client);
                }
            });
//            savedClients.add(repository.save(client));
        }

        return savedClients
                .stream()
                .sorted(Comparator.comparing(Client::getId))
                .toList();
    }

    @HttpIncomeRequestLog
    @Transactional
    @Override
    public RegistryResponce registerClient(Client client) {

        boolean isBlacklisted = blacklistRegistry.existsByDocumentId(client.getDocumentId());
        if (isBlacklisted) {
            throw new ClientException("Client has been banned");
        }

        Client saved = null;

        Optional<CheckResponce> check = check(Long.valueOf(client.getClientId()));
        if (check.isPresent()) {
            if (!check.get().getBlocked()) {
                saved = repository.save(client);
                try {
                    kafkaClientProducer.send(client.getId());
                }
                catch (Exception e) {
                    log.error("Ошибка отправки clientId={}: {}", client.getId(), e.getMessage(), e);
                }
            }
            else throw new ClientException("Client has been banned");
        }
//        client.setId(222L);
//        return client;
        return RegistryResponce.builder()
                .clientId(saved.getId())
                .build();
    }

    @Override
    public List<ClientDto> parseJson() {
        log.info("Parsing json");
        ObjectMapper mapper = new ObjectMapper();
        ClientDto[] clients = new ClientDto[0];
        try {
//            clients = mapper.readValue(new File("src/main/resources/MOCK_DATA.json"), ClientDto[].class);
            clients = new ClientDto[]{ClientDto.builder()
                    .firstName("first_name_1")
                    .build(),
                    ClientDto.builder()
                            .firstName("first_name_2")
                            .build()};
        } catch (Exception e) {
//            throw new RuntimeException(e);
            log.warn("Exception: ", e);
        }
        log.info("Found {} clients", clients.length);
        return Arrays.asList(clients);
    }

    @HttpOutcomeRequestLog
    @Override
    public Optional<CheckResponce> check(Long clientId) {
        String uri = "http://account_processing:8080/Dockerfile/" + clientId;
        log.info("Отправка HTTP-запроса к {}", uri);

        try {
            ResponseEntity<CheckResponce> response =
                    restTemplate.getForEntity(uri, CheckResponce.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Ошибка при обращении к {}: {}", uri, e.getMessage());
            return Optional.empty();
        }
    }

//    void clearMiddleName(List<ClientDto> dtos);

    @Override
    public Optional<Client> findById(Long id) {
        return repository.findById(id);
    }
}
