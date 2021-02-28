package academy.digitallab.store.product;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import academy.digitallab.store.product.repository.ProductRepository;
import academy.digitallab.store.product.service.ProductServiceImp;
import academy.digitallab.store.product.service.ProductServiceInt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepository;

    private ProductServiceInt productServiceInt;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        productServiceInt =new ProductServiceImp(productRepository);

        Product product= Product.builder()
                .id(1L)
                .name("Computer")
                .category(Category.builder().id(1L).build())
                .description("Computer core I9")
                .stock(Double.parseDouble("10"))
                .price(Double.parseDouble("6800000.00"))
                .status("Create")
                .createAt(new Date())
                .build();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
    }

    @Test
    public void when_getProductByIdValid_Then_ReturnProduct(){
        Product productFound = productServiceInt.getProduct(1L);
        Assertions.assertThat(productFound.getName()).isEqualTo("Computer");
    }

    @Test
    public void when_updateStockProductValid_Then_ReturnNewStock(){
        Product productFound =productServiceInt.updateStock(1L, Double.parseDouble("8"));
        Assertions.assertThat(productFound.getStock()).isEqualTo(18);
    }

}
