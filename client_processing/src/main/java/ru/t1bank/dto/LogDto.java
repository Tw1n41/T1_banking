package ru.t1bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("method_signature")
    private String methodSignature;

    @JsonProperty("exception_message")
    private String exceptionMessage;

    @JsonProperty("stack_trace")
    private String stackTrace;

    @JsonProperty("args")
    private Object[] args;
}
