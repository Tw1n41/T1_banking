package ru.t1bank.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class JwtRequestInterceptor implements ClientHttpRequestInterceptor {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public ClientHttpResponse intercept(
            org.springframework.http.HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        String token = jwtTokenUtil.generateToken("account-processing", new HashMap<>());

        request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        return execution.execute(request, body);
    }
}
