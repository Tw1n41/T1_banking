package ru.t1bank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1bank.dto.ClientProductDto;
import ru.t1bank.service.metrics.ClientProductService;

import java.util.List;

@RestController
@RequestMapping("/client-products")
@RequiredArgsConstructor
@Slf4j
public class ClientProductController {

    private final ClientProductService clientProductService;

    @PostMapping
    public ResponseEntity<ClientProductDto> create(@RequestBody ClientProductDto productDto) {
        return ResponseEntity.ok(clientProductService.create(productDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientProductDto> getById(@PathVariable Long id) {
        return clientProductService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClientProductDto>> getAll() {
        return ResponseEntity.ok(clientProductService.getAllServices());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientProductDto> update(@PathVariable Long id,
                                                   @RequestBody ClientProductDto productDto) {
        return ResponseEntity.ok(clientProductService.updService(id, productDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientProductService.delService(id);
        return ResponseEntity.noContent().build();
    }
}
