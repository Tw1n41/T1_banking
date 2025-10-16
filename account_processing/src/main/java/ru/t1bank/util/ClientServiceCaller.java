package ru.t1bank.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.t1bank.dto.ClientDto;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClientServiceCaller {

    private final RestTemplate restTemplate;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${client.service.url}")
    private String clientServiceUrl;

    public ClientServiceCaller(RestTemplate restTemplate, JwtTokenUtil jwtTokenUtil) {
        this.restTemplate = restTemplate;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public ClientDto getClient(Long id, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        String token = jwtTokenUtil.generateToken("account-service", claims.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ClientDto> response = restTemplate.exchange(
                clientServiceUrl + "/client/" + id,
                HttpMethod.GET,
                entity,
                ClientDto.class
        );

        return response.getBody();
    }
}
