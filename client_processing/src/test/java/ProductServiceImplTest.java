import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.t1bank.Product;
import ru.t1bank.dto.ProductDto;
import ru.t1bank.repository.ProductRepository;
import ru.t1bank.service.impl.ProductServiceImpl;
import ru.t1bank.util.ProductMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {
    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setId(1L);
        when(repository.save(any())).thenReturn(product);

        ProductDto productDto = productMapper.toDto(product);

        ProductDto saved = service.createProduct(productMapper.toDto(product));

        assertNotNull(saved);
        verify(repository).save(product);
    }
}