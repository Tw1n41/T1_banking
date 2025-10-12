package ru.t1bank.service.metrics;

import ru.t1bank.Client;
import ru.t1bank.dto.CheckResponce;
import ru.t1bank.dto.ClientDto;
import ru.t1bank.dto.RegistryResponce;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    public List<Client> registerClients(List<Client> clients);

    RegistryResponce registerClient(Client client);

    List<ClientDto> parseJson();

    Optional<Client> findById(Long id);

    public Optional<CheckResponce> check(Long clientId);
}
