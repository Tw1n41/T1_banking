import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1bank.Account;
import ru.t1bank.dto.TransactionDto;
import ru.t1bank.repository.AccountRepository;
import ru.t1bank.repository.PaymentRepository;
import ru.t1bank.repository.TransactionRepository;
import ru.t1bank.service.impl.TransactionProcessingServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionProcessingServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private TransactionProcessingServiceImpl transactionProcessingService;

    @Test
    void testDebitTransactionWithSufficientBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(1000));

        TransactionDto dto = TransactionDto.builder()
                .accountId(1L)
                .amount(BigDecimal.valueOf(500))
                .type("DEBIT")
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        transactionProcessingService.process("uuid-key", dto);

        assertEquals(BigDecimal.valueOf(500), account.getBalance());
    }

    @Test
    void testDebitTransactionInsufficientBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(400));

        TransactionDto dto = TransactionDto.builder()
                .accountId(1L)
                .amount(BigDecimal.valueOf(500))
                .type("DEBIT")
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        transactionProcessingService.process("uuid-key", dto);

        assertEquals(BigDecimal.valueOf(400), account.getBalance());
    }
}
