package ru.t1bank.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import ru.t1bank.dto.CheckRequest;
import ru.t1bank.dto.CheckResponce;


import java.util.Optional;

@Slf4j
public class CheckWebClient extends BaseWebClient {

    @Value("${integration.resource}")
    private String resource;

    public CheckWebClient(WebClient webClient) {
        super(webClient);
    }

    public Optional<CheckResponce> check(Integer id) {
        log.debug("Старт запроса с id {}", id);
        ResponseEntity<CheckResponce> post = null;
        try {
            CheckRequest request = CheckRequest.builder()
                    .clientId(id)
                    .build();

            post = this.post(
                    uriBuilder -> uriBuilder.path(resource).build(),
                    request,
                    CheckResponce.class);


        } catch (Exception httpStatusException) {
            throw httpStatusException;
        }

        log.debug("Финиш запроса с id {}", id);
        return Optional.ofNullable(post.getBody());
    }
}