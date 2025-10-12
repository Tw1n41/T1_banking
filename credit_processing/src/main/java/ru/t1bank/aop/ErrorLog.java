package ru.t1bank.aop;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "error_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "method_signature")
    private String methodSignature;

    @Column(name = "exception_message")
    private String exceptionMessage;

    @Column(name = "stack_trace")
    private String stackTrace;

    @Column(name = "args")
    private String args;

    @Column(name = "log_level")
    private String logLevel;
}
