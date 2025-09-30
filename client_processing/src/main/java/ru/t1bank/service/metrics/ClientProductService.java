package ru.t1bank.service.metrics;

import ru.t1bank.ClientProduct;
import ru.t1bank.dto.ClientProductDto;

import java.util.List;
import java.util.Optional;

public interface ClientProductService {

    ClientProductDto create(ClientProductDto clPDto);

    Optional<ClientProductDto> getById(Long id);

    List<ClientProductDto> getAllServices();

    ClientProductDto updService(Long id, ClientProductDto dto);

    void delService(Long id);
}
