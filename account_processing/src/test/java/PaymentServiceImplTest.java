import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1bank.Account;
import ru.t1bank.dto.PaymentDto;
import ru.t1bank.enums.PaymentStatus;
import ru.t1bank.mapper.PaymentMapper;
import ru.t1bank.repository.AccountRepository;
import ru.t1bank.repository.PaymentRepository;
import ru.t1bank.service.impl.PaymentServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testPaymentDebitEnoughBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000"));

        PaymentDto dto = new PaymentDto();
        dto.setAccountId(1L);
        dto.setAmount(new BigDecimal("500"));
        dto.setIsCredit(true);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(paymentMapper.toEntity(dto)).thenCallRealMethod();
        when(paymentMapper.toDto(any())).thenCallRealMethod();

        PaymentDto result = paymentService.payment("uuid", dto);

        assertEquals(new BigDecimal("500"), account.getBalance());
        assertEquals(PaymentStatus.CONFIRMED, result.getPaymentStatus());
    }

    @Test
    void testPaymentDebitInsufficientBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("100"));

        PaymentDto dto = new PaymentDto();
        dto.setAccountId(1L);
        dto.setAmount(new BigDecimal("500"));
        dto.setIsCredit(true);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(paymentMapper.toEntity(dto)).thenCallRealMethod();
        when(paymentMapper.toDto(any())).thenCallRealMethod();

        PaymentDto result = paymentService.payment("uuid", dto);

        assertTrue(result.isExpired());
        assertEquals(new BigDecimal("100"), account.getBalance());
    }
}
