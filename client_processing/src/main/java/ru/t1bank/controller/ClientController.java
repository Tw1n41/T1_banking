package ru.t1bank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1bank.Client;
import ru.t1bank.aop.annotation.LogMethod;
import ru.t1bank.aop.annotation.LoggableException;
import ru.t1bank.dto.ClientDto;
import ru.t1bank.dto.RegistryResponce;
import ru.t1bank.enums.Metrics;
import ru.t1bank.kafka.KafkaClientProducer;
import ru.t1bank.util.ClientMapper;
import ru.t1bank.repository.ClientRepository;
import ru.t1bank.service.metrics.ClientService;
import ru.t1bank.service.metrics.MetricService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;
    private final KafkaClientProducer kafkaClientProducer;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final MetricService metricService;

    @Value("${t1bank.kafka.topic.client_registration}")
    private String topic;

    @LogMethod
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        log.debug("Getting client with id ");
        return clientService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin")
    public String adminAccess() {
        return "Admin Board.";
    }

    @PostMapping("/register")
    public ResponseEntity<List<Client>> register(@RequestBody ClientDto clientDto) {
        log.info("Registering client: {}", clientDto);
        List<Client> clients = clientService.registerClients(
                List.of(clientMapper.toEntityWithId(clientDto))
        );
        Client client = clientMapper.toEntity(clientDto);
        RegistryResponce registryResponce = clientService.registerClient(client);
        metricService.incrementByName(Metrics.CLIENT_CONTROLLER_REQUEST_COUNT.getValue());
        return ResponseEntity.ok().body(clients);
    }
}