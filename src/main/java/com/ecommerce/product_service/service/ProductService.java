package com.ecommerce.product_service.service;

import com.ecommerce.product_service.core.service.ExBaseAbstractService;
import com.ecommerce.product_service.domain.Product;
import com.ecommerce.product_service.dto.ProductDto;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductService extends ExBaseAbstractService<ProductDto, Product, Long> {

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public String getModuleNameForLog() {
        return "[ProductService]";
    }
}
