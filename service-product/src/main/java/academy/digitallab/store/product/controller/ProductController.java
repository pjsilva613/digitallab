package academy.digitallab.store.product.controller;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import academy.digitallab.store.product.service.ProductServiceInt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    ProductServiceInt productServiceInt;

    Logger log= LoggerFactory.getLogger(ProductController.class);

    @GetMapping
    public ResponseEntity<List<Product>> listProduct(@RequestParam(name = "categoryId", required = false) Long categoryId){
        List<Product> listProducts;
        if (null == categoryId) {
           listProducts=productServiceInt.listAllProduct();
            if (listProducts.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        }else {
            listProducts= productServiceInt.findByCategory(Category.builder().id(categoryId).build());
            if (listProducts.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(listProducts);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        Product product= productServiceInt.getProduct(id);
        if ( null == product ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(bindingResult));
        }
        Product productDb = productServiceInt.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDb);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product){
        product.setId(id);
        Product productDb = productServiceInt.updateProduct(product);
        if (productDb == null){
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(productDb);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id){
        Product productDb = productServiceInt.deleteProduct(id);
        if (productDb == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDb);
    }

    @GetMapping(value = "/{id}/Quantity")
    public ResponseEntity<Product> updateStockProduct(@PathVariable("id") Long id, @RequestParam(name = "Quantity", required = true) Double Quantity){
        Product productDb = productServiceInt.updateStock(id, Quantity);
        if (productDb == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDb);
    }

    private String formatMessage(BindingResult bindingResult){

        List<Map<String, String>> errors= bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> {Map<String, String> error = new HashMap<>();
                log.info("Imprime error:    "+fieldError.getField()+" "+fieldError.getDefaultMessage());
                error.put(fieldError.getField(), fieldError.getDefaultMessage());
                return error;})
                .collect(Collectors.toList());

        ErrorMessage errorMessage= ErrorMessage.builder().code("01").Message(errors).build();

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString="";

        try {
            jsonString= objectMapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info(jsonString);
        return jsonString;
    }
}
