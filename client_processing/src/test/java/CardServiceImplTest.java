import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.t1bank.Card;
import ru.t1bank.dto.CardDto;
import ru.t1bank.repository.CardRepository;
import ru.t1bank.service.impl.CardServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CardServiceImplTest {
    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCardById_Found() {
        Card card = new Card();
        card.setId(1L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        Optional<CardDto> result = cardService.getCardById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetCardById_NotFound() {
        when(cardRepository.findById(any())).thenReturn(Optional.empty());
        Optional<CardDto> result = cardService.getCardById(999L);
        assertTrue(result.isEmpty());
    }
}
