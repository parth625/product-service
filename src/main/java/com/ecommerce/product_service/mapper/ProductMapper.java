package com.ecommerce.product_service.mapper;

import com.ecommerce.product_service.core.mapper.IBaseMapper;
import com.ecommerce.product_service.domain.Product;
import com.ecommerce.product_service.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends IBaseMapper<ProductDto, Product> {
}
