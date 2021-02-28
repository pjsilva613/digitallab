package academy.digitallab.store.product;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import academy.digitallab.store.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

@DataJpaTest
public class ProductRepositoryMockTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public  void When_FindByCategory_Then_ReturnListCategory(){

        Product product= Product.builder()
                .name("Computer")
                .category(Category.builder().id(1L).build())
                .description("Computer core I9")
                .stock(Double.parseDouble("10"))
                .price(Double.parseDouble("6800000.00"))
                .status("Create")
                .createAt(new Date())
                .build();
        productRepository.save(product);

        List<Product> listFounts = productRepository.findByCategory(product.getCategory());

        Assertions.assertThat(listFounts.size()).isEqualTo(3);

    }

}
