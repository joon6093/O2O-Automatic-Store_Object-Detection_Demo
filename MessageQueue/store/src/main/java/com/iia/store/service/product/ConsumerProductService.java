package com.iia.store.service.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iia.store.repository.product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class ConsumerProductService {
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ConsumerProductService(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "object-detection-results")
    @Transactional
    public void updateProductStock(String message) {
        try {
            Map<String, Object> updateInfo = objectMapper.readValue(message, Map.class);
            Long storeId = Long.valueOf((String) updateInfo.get("store-id"));
            Map<String, Map<String, Integer>> items = (Map<String, Map<String, Integer>>) updateInfo.get("items");

            items.forEach((productName, details) -> {
                Integer quantity = details.get("count");
                productRepository.findByStoreIdAndProductName(storeId, productName).ifPresentOrElse(
                        product -> {
                            product.updateStock(quantity);
                            log.info("Updated product '{}' in store {} with new quantity {}", productName, storeId, quantity);
                        },
                        () -> log.warn("Product '{}' not found in store {}", productName, storeId)
                );
            });
        } catch (IOException e) {
            log.error("Failed to deserialize Kafka message: {}", message, e);
        } catch (Exception e) {
            log.error("Error updating product stock: ", e);
        }
    }
}