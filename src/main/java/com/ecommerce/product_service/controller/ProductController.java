package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.core.controller.ExBaseAbstractController;
import com.ecommerce.product_service.dto.ProductDto;
import com.ecommerce.product_service.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController extends ExBaseAbstractController<ProductDto> {

    public ProductController(ProductService service) {
        super(service);
    }

    @Override
    public String getModuleNameForLog() {
        return "[ProductController]";
    }
}
