package com.iia.store.controller.product;

import com.iia.store.config.aop.AssignStoreId;
import com.iia.store.config.response.Response;
import com.iia.store.dto.product.*;
import com.iia.store.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @AssignStoreId
    @PostMapping("/products")
    public ResponseEntity<Response> create(@Valid @ModelAttribute ProductCreateRequest req) {
        ProductCreateResponse response = productService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(response));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Response> delete(@PathVariable(name = "id") Long id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }

    @GetMapping("/stores/products/{id}")
    public ResponseEntity<Response> readAll(@PathVariable(name = "id") Long id) {
        ProductListDto productList = productService.readAll(id);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(productList));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Response> read(@PathVariable(name = "id") Long id) {
        ProductDto product = productService.read(id);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(product));
    }

    @PutMapping("/products/stock")
    public ResponseEntity<Response> updateStock(@Valid @RequestBody StockUpdateRequest req) {
        productService.updateStock(req.getProductId(), req.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }
}