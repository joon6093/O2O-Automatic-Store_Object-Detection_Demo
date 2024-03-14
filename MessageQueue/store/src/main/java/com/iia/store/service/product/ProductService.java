package com.iia.store.service.product;

import com.iia.store.config.exception.ProductNotFoundException;
import com.iia.store.config.exception.StoreNotFoundException;
import com.iia.store.dto.image.ImageDto;
import com.iia.store.dto.product.*;
import com.iia.store.entity.product.Product;
import com.iia.store.entity.product.ProductImage;
import com.iia.store.entity.store.Store;
import com.iia.store.repository.product.ProductRepository;
import com.iia.store.repository.store.StoreRepository;
import com.iia.store.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final ImageService imageService;

    @Transactional
    public ProductCreateResponse create(ProductCreateRequest req) {
        Store store = storeRepository.findById(req.getStoreId()).orElseThrow(StoreNotFoundException::new);
        Product product = Product.builder()
                .name(req.getName())
                .price(req.getPrice())
                .store(store)
                .build();
        product.addImages(req.getImages().stream().map(i -> new ProductImage(i.getOriginalFilename())).collect(toList()));
        uploadImages(req.getImages(), product.getImages());
        productRepository.save(product);
        return new ProductCreateResponse(product.getId());
    }

    private void uploadImages(List<MultipartFile> fileImages, List<ProductImage> images) {
        IntStream.range(0, images.size()).forEach(i -> imageService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    @Transactional
    @PreAuthorize("@productGuard.check(#id)")
    public void delete(@Param("id")Long id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        deleteImages(product.getImages());
        product.removeImages(product.getImages());
        productRepository.delete(product);
    }

    private void deleteImages(List<ProductImage> images) {
        images.forEach(i -> imageService.delete(i.getUniqueName()));
    }

    public ProductListDto readAll(Long id) {
        List<Product> products = productRepository.findByStoreIdWithImages(id);
        List<SimpleProductDto> simpleProductDtos = products.stream()
                .map(product -> new SimpleProductDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImages().stream().findFirst()
                        .map(image -> new ImageDto(image.getId(), image.getOriginName(), image.getUniqueName()))
                        .orElse(null),
                        product.getCreatedAt()
                ))
                .collect(Collectors.toList());
        return new ProductListDto(simpleProductDtos);
    }

    public ProductDto read(Long id) {
        Product product = productRepository.findByIdWithImages(id).orElseThrow(ProductNotFoundException::new);
        return new ProductDto(product.getId(), product.getName(), product.getPrice(),product.getStock().getQuantity(),
                product.getImages().stream()
                        .map(image -> new ImageDto(image.getId(), image.getOriginName(), image.getUniqueName()))
                        .collect(Collectors.toList()),
                product.getCreatedAt(), product.getModifiedAt());
    }

    @Transactional
    public void updateStock(Long productId, int newQuantity) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        product.updateStock(newQuantity);
        productRepository.save(product);
    }
}
